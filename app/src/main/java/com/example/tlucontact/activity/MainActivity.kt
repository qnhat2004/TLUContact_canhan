package com.example.tlucontact.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.example.tlucontact.fragment.StaffFragment
import com.example.tlucontact.fragment.UnitFragment
import com.example.tlucontact.viewmodel.UserViewModel
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.databinding.ActivityMainBinding
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sử dụng Toolbar
        setSupportActionBar(binding.toolbar)

        // Kiểm tra trạng thái đăng nhập
        val sharedPref = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        val authToken = sharedPref.getString("AUTH_TOKEN", null)
        if (authToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Hiển thị UnitFragment mặc định khi khởi động
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, UnitFragment())
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_units
        }

        val options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
        startActivity(intent, options.toBundle())

        // Lấy thông tin người dùng qua ViewModel
        userViewModel.getUserInfo(this).observe(this) { userInfo ->
            if (userInfo != null) {
                Log.d("MainActivity", userInfo.toString())
                binding.tvUserName.text = "${userInfo.firstName} ${userInfo.lastName}"
                Glide.with(this).load(userInfo.imageUrl).into(binding.ivUserAvatar)
            } else {
                binding.tvUserName.text = "No user info found"
            }
        }

        // Xử lý sự kiện Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_units -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, UnitFragment())
                        .commit()
                    true
                }
                R.id.nav_staff -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StaffFragment())
                        .commit()
                    true
                }
                R.id.nav_student -> {
                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, StudentFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}