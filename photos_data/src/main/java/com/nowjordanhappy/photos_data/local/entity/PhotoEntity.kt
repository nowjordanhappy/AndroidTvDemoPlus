package com.nowjordanhappy.photos_data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "owner")
    val owner: String,
    @ColumnInfo(name = "secret")
    val secret: String?,
    @ColumnInfo(name = "server")
    val server: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "is_public")
    val isPublic:Boolean,
    @ColumnInfo(name = "is_family")
    val isFamily: Boolean,
    @ColumnInfo(name = "date_upload")
    val dateUpload: Long,
    @ColumnInfo(name = "ownername")
    val ownername: String,
    @ColumnInfo(name = "date_saved")
    val dateSaved: Long,
    )