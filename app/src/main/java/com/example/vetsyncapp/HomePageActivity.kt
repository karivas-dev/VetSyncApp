package com.example.vetsyncapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.vetsyncapp.databinding.FragmentHomeBinding


class HomePageActivity : AppCompatActivity() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.mainHostFragment)
        binding.btnNav.setupWithNavController(navController)
    }
}