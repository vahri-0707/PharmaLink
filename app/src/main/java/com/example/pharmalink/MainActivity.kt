package com.example.pharmalink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set up the BottomNavigationView with the NavController
        bottomNav.setupWithNavController(navController)

        // Add a destination changed listener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current destination is the CartFragment
            if (destination.id == R.id.cartFragment) {
                // Hide or customize the BottomNavigationView as needed
                bottomNav.visibility = View.GONE
            } else {
                // Show the BottomNavigationView for other fragments
                bottomNav.visibility = View.VISIBLE
            }
        }
    }
}