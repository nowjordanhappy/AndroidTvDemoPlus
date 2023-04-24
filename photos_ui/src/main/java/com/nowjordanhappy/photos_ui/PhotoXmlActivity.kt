package com.nowjordanhappy.photos_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nowjordanhappy.photos_ui.databinding.ActivityPhotoXmlBinding
import dagger.hilt.EntryPoint

@EntryPoint
class PhotoXmlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoXmlBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoXmlBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}