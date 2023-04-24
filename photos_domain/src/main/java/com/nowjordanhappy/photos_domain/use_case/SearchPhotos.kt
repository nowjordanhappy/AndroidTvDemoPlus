package com.nowjordanhappy.photos_domain.use_case

import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.core_ui.domain.UIComponent
import com.nowjordanhappy.photos_domain.data.DataState
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchPhotos(
    private val repository: PhotoRepository

) {
    fun execute(
        query: String,
        page: Int,
        pageSize: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<Photo>>> = flow {
        try {
            emit(DataState.Loading(
                progressBarState = ProgressBarState.Loading
            ))

            if(isNetworkAvailable){
                try {
                    repository.searchPhotoRemote(
                        query = query,
                        page = page,
                        pageSize = pageSize
                    )
                }catch (e: Exception){
                    emit(DataState.Response(UIComponent.None(e.message ?: "API Error")))
                }
            }

            val localPhotos = repository.searchPhotoRemote(
                query = query,
                page = page,
                pageSize = pageSize
            )

            emit(DataState.Data(localPhotos))

        } catch (e: Exception) {
            emit(DataState.Response(UIComponent.None(e.message ?: "Unknown Error")))
        }finally {
            emit(DataState.Loading(
                progressBarState = ProgressBarState.Idle
            ))
        }
    }
}