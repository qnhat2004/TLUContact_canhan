package com.example.tlucontact_canhan.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tlucontact.fragment.StaffFragment
import com.example.tlucontact.fragment.StudentFragment
import com.example.tlucontact_canhan.ProfileFragment
import com.example.tlucontact_canhan.fragment.ContactUnitFragment
import com.example.tlucontact_canhan.R
import com.example.tlucontact_canhan.databinding.ActivityMainBinding
import com.example.tlucontact_canhan.repository.AuthRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val authRepository = AuthRepository(this)
        val authToken = authRepository.getToken()
        if (authToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, ContactUnitFragment())
                .addToBackStack(null)
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_units
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_units -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ContactUnitFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.nav_staff -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StaffFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.nav_student -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StudentFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}