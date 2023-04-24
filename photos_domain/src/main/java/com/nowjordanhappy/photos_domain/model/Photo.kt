package com.nowjordanhappy.photos_domain.model

data class Photo (
    val id: String,
    val owner: String,
    val secret: String?,
    val server: String?,
    val imageUrl: String?,
    val title: String,
    val isPublic:Boolean,
    val isFamily: Boolean,
    val dateUpload: String,
    val ownername: String
)