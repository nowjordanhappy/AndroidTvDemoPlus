package com.nowjordanhappy.photos_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.nowjordanhappy.photos_ui.databinding.ActivityPhotoXmlBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoXmlActivity : FragmentActivity() {
    private lateinit var binding: ActivityPhotoXmlBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoXmlBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
}