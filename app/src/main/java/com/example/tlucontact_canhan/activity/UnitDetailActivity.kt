package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tlucontact.adapter.ContactUnitAdapter
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.databinding.ActivityUnitDetailBinding
import com.example.tlucontact_canhan.model.UnitDetailDTO
import com.example.tlucontact_canhan.model.UnitListItem
import com.example.tlucontact_canhan.repository.ContactUnitRepository
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModel
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnitDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUnitDetailBinding
    private lateinit var viewModel: ContactUnitViewModel
    private lateinit var childUnitAdapter: ContactUnitAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ContactUnitViewModelFactory(ContactUnitRepository(this)))
            .get(ContactUnitViewModel::class.java)

        // Initialize RecyclerView for child units
        childUnitAdapter = ContactUnitAdapter(emptyList()) { unit ->
            // Mở UnitDetailActivity cho đơn vị con
            val intent = Intent(this, UnitDetailActivity::class.java).apply {
                putExtra("unit_id", unit.contactUnit.id)
            }
            startActivity(intent)
        }

        binding.rvChildUnits.apply {
            layoutManager = LinearLayoutManager(this@UnitDetailActivity)
            adapter = childUnitAdapter
        }

        // Lấy unitId từ Intent
        val unitId = intent.getLongExtra("unit_id", -1)
        if (unitId == -1L) {
            Toast.makeText(this, "Không tìm thấy ID đơn vị!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Lấy chi tiết đơn vị
        coroutineScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val result = viewModel.getUnitById(unitId)
            binding.progressBar.visibility = View.GONE

            result.onSuccess { unitDetail ->
                Log.d("UnitDetailActivity", "Unit Detail: $unitDetail")

                // Hiển thị thông tin đơn vị
                binding.tvUnitName.text = unitDetail.name
                binding.tvUnitCode.text = unitDetail.unitCode
                binding.tvUnitEmail.text = unitDetail.email ?: "Không có email"
                binding.tvUnitAddress.text = unitDetail.address ?: "Không có địa chỉ"
                binding.tvUnitFax.text = unitDetail.fax ?: "Không có fax"

                // Hiển thị logo nếu có
                if (!unitDetail.logoUrl.isNullOrEmpty()) {
                    Glide.with(this@UnitDetailActivity)
                        .load(unitDetail.logoUrl)
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(binding.ivUnitImage)
                }

                // Hiển thị đơn vị cha (nếu có)
                if (unitDetail.parentUnitId != null) {
                    Log.d("UnitDetailActivity", "Parent Unit ID: ${unitDetail.parentUnitId}")
                    binding.tvParentUnit.visibility = View.VISIBLE
                    binding.tvParentLabel.visibility = View.VISIBLE
                    val parentResult = viewModel.getUnitById(unitDetail.parentUnitId!!)
                    parentResult.onSuccess { parentUnit ->
                        Log.d("Parent id", "Parent Unit: ${parentUnit.id}")
                        binding.tvParentUnit.text = parentUnit.name
                        binding.tvParentUnit.setOnClickListener {
                            val intent = Intent(this@UnitDetailActivity, UnitDetailActivity::class.java).apply {
                                putExtra("unit_id", parentUnit.id)
                            }
                            startActivity(intent)
                        }
                    }.onFailure { exception ->
                        Log.e("UnitDetailActivity", "Failed to load parent unit: ${exception.message}")
                        binding.tvParentUnit.text = "Không thể tải đơn vị cha"
                    }
                } else {
                    Log.d("UnitDetailActivity", "No parent unit found")
                    binding.tvParentUnit.visibility = View.GONE
                    binding.tvParentLabel.visibility = View.GONE
                }

                // Safely handle null childUnitIds
                val childUnitIds = unitDetail.childUnitIds ?: emptyList()
                Log.d("UnitDetailActivity", "Child Unit IDs: $childUnitIds")

                if (childUnitIds.isEmpty()) {
                    Log.d("UnitDetailActivity", "No child units found")
                    binding.rvChildUnits.visibility = View.GONE
                    binding.tvChildUnitsLabel.visibility = View.GONE
                } else {
                    binding.rvChildUnits.visibility = View.VISIBLE
                    binding.tvChildUnitsLabel.visibility = View.VISIBLE
                    val childUnits = mutableListOf<UnitDetailDTO>()
                    // Fetch child units in parallel
                    childUnitIds.map { childId ->
                        launch(Dispatchers.IO) {
                            val childResult = viewModel.getUnitById(childId)
                            childResult.onSuccess { childUnit ->
                                Log.d("UnitDetailActivity", "Child Unit: $childUnit")
                                synchronized(childUnits) {
                                    childUnits.add(childUnit)
                                }
                            }.onFailure { exception ->
                                Log.e("UnitDetailActivity", "Failed to load child unit $childId: ${exception.message}")
                            }
                        }
                    }.forEach { it.join() } // Wait for all coroutines to complete
                    withContext(Dispatchers.Main) {
                        Log.d("UnitDetailActivity", "Child Units Loaded: $childUnits")
                        // Convert List<UnitDetailDTO> to List<UnitListItem>
                        val unitListItems = childUnits.map { UnitListItem.Unit_(it) }
                        childUnitAdapter.updateItems(unitListItems)
                    }
                }

                // Nút gọi điện
                binding.btnUnitCall.setOnClickListener {
                    Toast.makeText(this@UnitDetailActivity, "Chức năng gọi điện chưa được triển khai (thiếu trường phone)", Toast.LENGTH_SHORT).show()
                }

                // Nút Gửi email
                binding.btnUnitEmail.setOnClickListener {
                    if (unitDetail.email != null) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${unitDetail.email}")
                            putExtra(Intent.EXTRA_SUBJECT, "Liên hệ với ${unitDetail.name}")
                        }
                        startActivity(Intent.createChooser(intent, "Gửi email"))
                    } else {
                        Toast.makeText(this@UnitDetailActivity, "Không có email để gửi", Toast.LENGTH_SHORT).show()
                    }
                }

                // Nút Nhắn tin
                binding.btnUnitMessage.setOnClickListener {
                    Toast.makeText(this@UnitDetailActivity, "Chức năng nhắn tin chưa được triển khai (thiếu trường phone)", Toast.LENGTH_SHORT).show()
                }

                // Nút chia sẻ
                binding.btnUnitShare.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, """
                            Tên đơn vị: ${unitDetail.name}
                            Mã đơn vị: ${unitDetail.unitCode}
                            Email: ${unitDetail.email ?: "Không có email"}
                            Địa chỉ: ${unitDetail.address ?: "Không có địa chỉ"}
                            Fax: ${unitDetail.fax ?: "Không có fax"}
                        """.trimIndent())
                    }
                    startActivity(Intent.createChooser(intent, "Chia sẻ thông tin đơn vị"))
                }
            }.onFailure { exception ->
                Log.e("UnitDetailActivity", "Failed to load unit: ${exception.message}")
                Toast.makeText(this@UnitDetailActivity, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Nút quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.coroutineContext[Job]?.cancel()
    }
}