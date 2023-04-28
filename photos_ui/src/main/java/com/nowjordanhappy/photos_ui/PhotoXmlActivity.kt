package com.nowjordanhappy.photos_ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.nowjordanhappy.photos_ui.databinding.ActivityPhotoXmlBinding
import com.nowjordanhappy.photos_ui.detail_photo.DetailPhotoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoXmlActivity : FragmentActivity() {
    private lateinit var binding: ActivityPhotoXmlBinding

    private lateinit var appBarConfiguration : AppBarConfiguration

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoXmlBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController)
    }


    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v("Detail", "onKeyDown: $keyCode")
        if(navController.currentDestination?.id == R.id.detailPhotoFragment){
            Log.v("Detail", "onKeyDown detail")

            //(navHostFragment.childFragmentManager.findFragmentById(R.id.detailPhotoFragment) as? DetailPhotoFragment)?.let {
            (navHostFragment.childFragmentManager.primaryNavigationFragment as? DetailPhotoFragment)?.let {
                Log.v("Detail", "onKeyDown detail fragment")

                if(keyCode == 21){
                    //left
                    it.leftAction()
                }
                if(keyCode == 22){
                    //right
                    it. rightAction()
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }*/
}