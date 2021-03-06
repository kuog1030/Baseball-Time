package com.gillian.baseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gillian.baseball.databinding.ActivityMainBinding
import com.gillian.baseball.login.UserManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)

        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.teamFragment -> setBottomNavigation(true)
                R.id.allGamesFragment -> setBottomNavigation(true)
                R.id.allBroadcastFragment -> if (UserManager.userId == "") {
                    setBottomNavigation(false)
                } else {
                    setBottomNavigation(true)
                }
                else -> setBottomNavigation(false)
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