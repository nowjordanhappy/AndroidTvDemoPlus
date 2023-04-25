package com.nowjordanhappy.photos_ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.nowjordanhappy.photos_ui.databinding.ActivityPhotoXmlBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoXmlActivity : FragmentActivity() {
    private lateinit var binding: ActivityPhotoXmlBinding

    private lateinit var appBarConfiguration : AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoXmlBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController)
    }
}