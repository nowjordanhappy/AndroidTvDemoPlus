package com.nowjordanhappy.photos_domain.use_case

import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.core_ui.domain.UIComponent
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_domain.repository.PhotoRepository
import com.nowjordanhappy.photos_domain.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPhotoDetail(
    private val repository: PhotoRepository

) {
    fun execute(
        id: Int
    ): Flow<DataState<Photo>> = flow {
        try {
            emit(DataState.Loading(
                progressBarState = ProgressBarState.Loading
            ))

            /*if(isNetworkAvailable){
                try {
                    repository.getRecentPhotosRemote(
                        page = page,
                        pageSize = pageSize
                    )
                }catch (e: Exception){
                    emit(DataState.error(e.message ?: "API Error"))
                }
            }*/

            val localPhoto = repository.getPhotoById(id) ?: throw  Exception("That hero does not exist in the cache")

            emit(DataState.Data(localPhoto))

        } catch (e: Exception) {
            emit(DataState.Response(UIComponent.None(e.message ?: "Unknown Error")))
        }finally {
            emit(DataState.Loading(
                progressBarState = ProgressBarState.Idle
            ))
        }
    }
}