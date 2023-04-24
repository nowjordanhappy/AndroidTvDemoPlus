package com.nowjordanhappy.photos.domain.use_case

data class PhotoUseCases (
    val searchPhotos: SearchPhotos,
    val getRecentPhotos: GetRecentPhotos,
    val getPhotoDetail: GetPhotoDetail
)