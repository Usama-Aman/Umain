package com.sam.umain.presentation

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sam.umain.R
import com.sam.umain.presentation.restaurants.RestaurantFragment
import com.sam.umain.utils.Utility.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        replaceFragment(
            activity = this@MainActivity,
            fragmentContainer = R.id.fragmentContainer,
            fragment = RestaurantFragment.newInstance(),
            isBackStack = false
        )
    }
}