package com.nowjordanhappy.photos_data.repository

import android.util.Log
import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.core.domain.StatResponse
import com.nowjordanhappy.photos_data.local.entity.PhotoDao
import com.nowjordanhappy.photos_data.mapper.PhotoEntityMapper
import com.nowjordanhappy.photos_data.remote.PhotoApi
import com.nowjordanhappy.photos_data.remote.dto.mapper.PhotoDtoMapper
import com.nowjordanhappy.photos_data.remote.utils.ApiException
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_domain.repository.PhotoRepository

class PhotoRepositoryImpl(
    private val photoDao: PhotoDao,
    private val service: PhotoApi,
    private val entityMapper: PhotoEntityMapper,
    private val dtoMapper: PhotoDtoMapper,
): PhotoRepository {
    @Throws(ApiException::class)
    override suspend fun searchPhotoRemote(query: String, page: Int, pageSize: Int): List<Photo> {
        val photos = getPhotosFromNetwork(
            page = page,
            pageSize = pageSize,
            query = query
        )

        photoDao.insertPhotos(photos.map { entityMapper.mapFromDomainModel(it) })
        return photos
    }

    @Throws(ApiException::class)
    private suspend fun getPhotosFromNetwork(
        page: Int,
        pageSize: Int,
        query: String,
    ): List<Photo>{
        val response = service.search(
            method = Constants.METHOD_SEARCH,
            apiKey = Constants.API_KEY,
            text = query,
            extras = Constants.API_PHOTO_EXTRAS,
            format = Constants.API_FORMAT,
            //perPage = Constants.API_SEARCH_PER_PAGE,
            perPage = pageSize,
            page = page,
            sort = Constants.API_SEARCH_SORT_BY,
            nojsoncallback = Constants.API_NOJSONCALLBACK,
        )

        return if (response.stat == StatResponse.ok.name) {
            (response.photos?.photo ?: emptyList())
                .mapNotNull {dto->
                    dtoMapper.mapToDomainModel(dto)
                }
        } else kotlin.run {
            throw ApiException(response.message ?: "Error in the API")
        }
    }


    override suspend fun searchPhotoLocal(query: String, page: Int, pageSize: Int): List<Photo> {
        val localPhotos = photoDao.searchRecipes(
            query = query,
            pageSize = pageSize,
            page = page
        )

        return localPhotos.map { entityMapper.mapToDomainModel(it) }
    }

    override suspend fun getRecentPhotosRemote(page: Int, pageSize: Int): List<Photo> {
        val photos = getRecentPhotosFromNetwork(
            page = page,
            pageSize = pageSize,
        )

        /*if(page == 1){
            photoDao.deleteAllPhotos()
        }*/

        val list = photos.map { entityMapper.mapFromDomainModel(it) }
        photoDao.insertPhotos(list)

        return photos
    }

    @Throws(ApiException::class)
    private suspend fun getRecentPhotosFromNetwork(
        page: Int,
        pageSize: Int,
    ): List<Photo>{
        val response = service.getRecent(
            method = Constants.METHOD_PHOTOS_GET_RECENT,
            apiKey = Constants.API_KEY,
            extras = Constants.API_PHOTO_EXTRAS,
            format = Constants.API_FORMAT,
            perPage = pageSize,
            page = page,
            nojsoncallback = Constants.API_NOJSONCALLBACK,
        )


        return if (response.stat == StatResponse.ok.name) {
            (response.photos?.photo ?: emptyList())
                .mapNotNull {dto->
                    dtoMapper.mapToDomainModel(dto)
                }
        } else kotlin.run {
            throw ApiException(response.message ?: "Error in the API")
        }
    }

    override suspend fun getRecentPhotosLocal(page: Int, pageSize: Int): List<Photo> {
        val localPhotos = photoDao.getRecentRecipes(
            pageSize = pageSize,
            page = page
        )

        val list = localPhotos.map { entityMapper.mapToDomainModel(it) }
        return localPhotos.map { entityMapper.mapToDomainModel(it) }
    }

    override suspend fun getPhotoById(id: Int): Photo? {
        return photoDao.getPhotoById(id)?.let {
            entityMapper.mapToDomainModel(it)
        }
    }
}