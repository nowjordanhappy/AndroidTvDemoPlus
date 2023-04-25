package com.nowjordanhappy.photos_ui.search

import com.nowjordanhappy.photos_domain.model.Photo

data class PhotoSelected(
    val photo: Photo? = null,
    val index: Int = -1
)
