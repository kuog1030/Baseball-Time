package com.gillian.baseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.gillian.baseball.databinding.ActivityMainBinding
import com.gillian.baseball.ext.toInningCount

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
                R.id.navigation_all_games -> {
                    navController.navigate(NavigationDirections.navigationToAllGames())
                    true
                }
                R.id.navigation_stat -> {
                    navController.navigate(NavigationDirections.navigationToStat())
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.gameFragment -> setBottomNavigation(false)
                else -> setBottomNavigation(true)
            }
        }

    }

    private fun setBottomNavigation(visible: Boolean) {
        if (visible) {
            binding.bottomNavView.visibility = VISIBLE
        } else {
            binding.bottomNavView.visibility = GONE
        }
    }
}