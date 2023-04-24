package com.nowjordanhappy.photos_data.remote

import com.nowjordanhappy.photos_data.remote.dto.GetGalleryInfoResponse
import com.nowjordanhappy.photos_data.remote.dto.GetRecentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {
    @GET("rest/")
    suspend fun getGalleryInfo(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("gallery_id") galleryId: String,
        @Query("limit") limit: Int,
        @Query("short_limit") shortLimit: Int,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
    ): GetGalleryInfoResponse

    @GET("rest/")
    suspend fun search(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("text") text: String,
        @Query("extras") extras: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
    ): GetRecentResponse

    @GET("rest/")
    suspend fun getRecent(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("extras") extras: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
    ): GetRecentResponse

    companion object{
        const val BASE_URL = "https://www.flickr.com/services/"
    }
}