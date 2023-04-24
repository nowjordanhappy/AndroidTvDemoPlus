package com.nowjordanhappy.photos.domain.use_case

import com.nowjordanhappy.domain.ProgressBarState
import com.nowjordanhappy.domain.UIComponent
import com.nowjordanhappy.photos.domain.data.DataState
import com.nowjordanhappy.photos.domain.model.Photo
import com.nowjordanhappy.photos.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecentPhotos(
    private val repository: PhotoRepository

) {
    fun execute(
        page: Int,
        pageSize: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<Photo>>> = flow {
        try {
            emit(DataState.Loading(
                progressBarState = com.nowjordanhappy.domain.ProgressBarState.Loading
            ))

            if(isNetworkAvailable){
                try {
                    repository.getRecentPhotosRemote(
                        page = page,
                        pageSize = pageSize
                    )
                }catch (e: Exception){
                    emit(DataState.Response(com.nowjordanhappy.domain.UIComponent.None(e.message ?: "API Error")))
                }
            }

            val localPhotos = repository.getRecentPhotosLocal(
                page = page,
                pageSize = pageSize
            )

            emit(DataState.Data(localPhotos))

        } catch (e: Exception) {
            emit(DataState.Response(com.nowjordanhappy.domain.UIComponent.None(e.message ?: "Unknown Error")))
        }finally {
            emit(DataState.Loading(
                progressBarState = com.nowjordanhappy.domain.ProgressBarState.Idle
            ))
        }
    }
}