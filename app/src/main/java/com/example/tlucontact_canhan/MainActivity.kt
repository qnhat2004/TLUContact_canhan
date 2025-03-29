package com.example.tlucontact_canhan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.tlucontact_canhan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        startActivity(intent)
        val options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent, options.toBundle())

        // Xử lý sự kiện Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_units -> {
                    val intent = Intent(this, UnitListActivity::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.nav_staff -> {
//                    val intent = Intent(this, StaffListActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
                else -> false
            }
        }
    }
}