package com.example.tlucontact_canhan.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tlucontact.adapter.ContactUnitAdapter
import com.example.tlucontact_canhan.activity.UnitDetailActivity
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.databinding.FragmentUnitBinding
import com.example.tlucontact_canhan.repository.ContactUnitRepository
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModel
import com.example.tlucontact_canhan.viewmodel.ContactUnitViewModelFactory

class ContactUnitFragment : Fragment() {
    private lateinit var viewModel: ContactUnitViewModel
    private lateinit var unitAdapter: ContactUnitAdapter
    private lateinit var sortDirection: String
    private var _binding: FragmentUnitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortDirection = "asc" // Default sort direction

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ContactUnitViewModelFactory(ContactUnitRepository(requireContext())))
            .get(ContactUnitViewModel::class.java)

        // Initialize RecyclerView and Adapter
        unitAdapter = ContactUnitAdapter(emptyList()) { unit ->
            val intent = Intent(requireContext(), UnitDetailActivity::class.java).apply {
                putExtra("unit_id", unit.contactUnit.id)
                putExtra("unit_name", unit.contactUnit.name)
                putExtra("unit_email", unit.contactUnit.email)
                putExtra("unit_address", unit.contactUnit.address)
                putExtra("unit_code", unit.contactUnit.unitCode)
                putExtra("unit_fax", unit.contactUnit.fax)
            }
            startActivity(intent)
        }

        binding.rvUnits.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = unitAdapter
        }

        // Observe unit list
        viewModel.units.observe(viewLifecycleOwner) { units ->
            unitAdapter.updateItems(units)
            binding.rvUnits.scrollToPosition(0)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        // Observe error
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Set swipe-to-refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.etUnitSearch.setText("") // Xóa query tìm kiếm khi làm mới
            viewModel.loadFirstPage(sortField = "name", sortDirection = sortDirection)
        }

        // Handle scrolling to load more data
        binding.rvUnits.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItem >= totalItemCount - 5 && viewModel.hasMoreData.value == true) {
                    viewModel.loadNextPage()
                }
            }
        })

        // Handle search
        binding.etUnitSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchUnitDetailDTOs(s.toString())
            }
        })

        // Load the first page
        viewModel.loadFirstPage(sortField = "name", sortDirection = sortDirection)

        // Handle sort button click
        binding.btnSort.setOnClickListener {
            sortDirection = if (sortDirection == "asc") "desc" else "asc"
            binding.btnSort.setImageResource(
                if (sortDirection == "asc") R.drawable.ic_sort_ascending
                else R.drawable.ic_sort_descending
            )
            viewModel.loadFirstPage(sortField = "name", sortDirection = sortDirection)
        }

        // Cập nhật icon ban đầu của nút
        binding.btnSort.setImageResource(R.drawable.ic_sort_ascending)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}