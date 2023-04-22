package com.nowjordanhappy.photos.domain.use_case

import com.nowjordanhappy.photos.data.remote.PhotoApi
import kotlinx.coroutines.flow.Flow

class SearchPhotos(
    private val service: PhotoApi
) {
    fun execute(page: Int,
                query: String,
                isNetworkAvailable: Boolean): Flow<>
}