package com.nowjordanhappy.photos_domain.repository

import com.nowjordanhappy.photos_domain.model.Photo

interface PhotoRepository {
    suspend fun searchPhotoRemote(
        query: String,
        page: Int,
        pageSize: Int
    ):List<Photo>

    suspend fun searchPhotoLocal(
        query: String,
        page: Int,
        pageSize: Int
    ):List<Photo>

    suspend fun getRecentPhotosRemote(
        page: Int,
        pageSize: Int
    ):List<Photo>

    suspend fun getRecentPhotosLocal(
        page: Int,
        pageSize: Int
    ):List<Photo>

    suspend fun getPhotoById(
        id: Int
    ): Photo?
}