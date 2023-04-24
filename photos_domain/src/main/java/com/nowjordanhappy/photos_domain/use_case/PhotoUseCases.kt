package com.nowjordanhappy.photos_domain.use_case

data class PhotoUseCases (
    val searchPhotos: SearchPhotos,
    val getRecentPhotos: GetRecentPhotos,
    val getPhotoDetail: GetPhotoDetail
)