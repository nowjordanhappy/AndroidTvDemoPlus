package com.nowjordanhappy.photos.data.remote.dto

import com.google.gson.annotations.SerializedName

open class BaseResponse (
    val stat: String? = null,
    val code: Long? = null,
    val message: String? = null
)
