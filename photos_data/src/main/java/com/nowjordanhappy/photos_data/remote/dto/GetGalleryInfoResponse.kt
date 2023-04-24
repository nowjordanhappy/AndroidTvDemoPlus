package com.nowjordanhappy.photos_data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetGalleryInfoResponse (
    val gallery: Gallery? = null,
): BaseResponse()

data class Gallery (
    val id: String? = null,

    @SerializedName("gallery_id")
    val galleryID: String? = null,

    val url: String? = null,
    val owner: String? = null,
    val username: String? = null,
    val iconserver: String? = null,
    val iconfarm: Long? = null,

    @SerializedName("primary_photo_id")
    val primaryPhotoID: String? = null,

    @SerializedName("date_create")
    val dateCreate: String? = null,

    @SerializedName("date_update")
    val dateUpdate: String? = null,

    @SerializedName("count_photos")
    val countPhotos: Long? = null,

    @SerializedName("count_videos")
    val countVideos: Long? = null,

    @SerializedName("count_total")
    val countTotal: Long? = null,

    @SerializedName("count_views")
    val countViews: String? = null,

    @SerializedName("count_comments")
    val countComments: Long? = null,

    val title: Description? = null,
    val description: Description? = null,

    @SerializedName("sort_group")
    val sortGroup: String? = null,

    @SerializedName("cover_photos")
    val coverPhotos: CoverPhotos? = null,

    @SerializedName("current_state")
    val currentState: String? = null,

    @SerializedName("primary_photo_server")
    val primaryPhotoServer: String? = null,

    @SerializedName("primary_photo_farm")
    val primaryPhotoFarm: Long? = null,

    @SerializedName("primary_photo_secret")
    val primaryPhotoSecret: String? = null
)

data class CoverPhotos (
    val photo: List<PhotoDto>? = null
)

data class PhotoGallery (
    val id: String? = null,
    val url: String? = null,
)

data class Description (
    @SerializedName("_content")
    val content: String? = null
)

