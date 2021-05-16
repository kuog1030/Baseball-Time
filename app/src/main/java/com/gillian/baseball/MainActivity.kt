package com.gillian.baseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.gillian.baseball.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)

        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_team -> {
                    navController.navigate(NavigationDirections.navigationToTeam())
                    true
                }
                R.id.navigation_game -> {
                    navController.navigate(NavigationDirections.navigationToOrder())
                    true
                }
                R.id.navigation_stat -> {
                    navController.navigate(NavigationDirections.navigationToStat())
                    true
                }
                else -> false
            }
        }

    }
}