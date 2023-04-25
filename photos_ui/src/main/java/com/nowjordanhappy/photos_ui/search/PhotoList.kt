package com.nowjordanhappy.photos_ui.search

import com.nowjordanhappy.photos_domain.model.Photo

data class PhotoListState(
    val photos: List<Photo> = emptyList(),
    val gridModeOn: Boolean = false
)
