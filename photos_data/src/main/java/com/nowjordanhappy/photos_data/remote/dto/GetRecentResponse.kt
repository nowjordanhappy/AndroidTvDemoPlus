package com.nowjordanhappy.photos_data.remote.dto

data class GetRecentResponse (
    val photos: Photos? = null,
): BaseResponse()

data class Photos (
    val page: Long? = null,
    val pages: Long? = null,
    val perpage: Long? = null,
    val total: Long? = null,
    val photo: List<PhotoDto>? = null
)
data class PhotoDto (
    val id: String? = null,
    val owner: String? = null,
    val secret: String? = null,
    val server: String? = null,
    val farm: Long? = null,
    val title: String? = null,
    val ispublic: Long? = null,
    val isfriend: Long? = null,
    val isfamily: Long? = null,
    val dateupload: String? = null,
    val ownername: String? = null
)