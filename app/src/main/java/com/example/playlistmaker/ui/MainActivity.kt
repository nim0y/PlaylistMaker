package com.example.playlistmaker.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.view_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationBar.setupWithNavController(navController)
        showNavBar()
    }

    private fun showNavBar() {
        val navBar = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        navBar.visibility = View.VISIBLE
    }
}