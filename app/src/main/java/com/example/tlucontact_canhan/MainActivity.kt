package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.tlucontact_canhan.databinding.ActivityMainBinding
import com.example.tlucontact_canhan.fragment.StaffFragment
import com.example.tlucontact_canhan.fragment.UnitFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sử dụng Toolbar
        setSupportActionBar(binding.toolbar)

        // Hiển thị UnitFragment mặc định khi khởi động
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainer, UnitFragment())
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_units
        }

        startActivity(intent)
        val options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent, options.toBundle())

        // Xử lý sự kiện Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_units -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, UnitFragment())
                        .commit()
                    true
                }
                R.id.nav_staff -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, StaffFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}