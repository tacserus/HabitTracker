package com.example.habittracker.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.habittracker.R
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navigationDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationDrawerLayout = findViewById(R.id.drawer_layout)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setupWithNavController(navController)

        navigationView.setCheckedItem(R.id.nav_home)

        navigationView.setNavigationItemSelectedListener { option ->
            when (option.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.habitViewPagerFragment)
                    navigationDrawerLayout.closeDrawers()
                    option.isChecked = true
                    true
                }
                R.id.nav_info -> {
                    navController.navigate(R.id.infoFragment)
                    navigationDrawerLayout.closeDrawers()
                    option.isChecked = true
                    true
                }
                else -> false
            }
        }
    }
}