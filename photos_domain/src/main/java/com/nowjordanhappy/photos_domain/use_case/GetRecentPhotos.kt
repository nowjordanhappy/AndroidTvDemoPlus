package com.nowjordanhappy.photos_domain.use_case

import android.util.Log
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.core_ui.domain.UIComponent
import com.nowjordanhappy.photos_domain.data.DataState
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_domain.repository.PhotoRepository
import kotlinx.coroutines.delay
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
                progressBarState = ProgressBarState.Loading
            ))
            //delay(2000)

            Log.v("SearchGridVM", "isNetworkAvailable: ${isNetworkAvailable}")

            if(isNetworkAvailable){
                try {
                    repository.getRecentPhotosRemote(
                        page = page,
                        pageSize = pageSize
                    )
                }catch (e: Exception){
                    Log.v("SearchGridVM", "None: ${e.message}")
                    emit(DataState.Response(UIComponent.None(e.message ?: "API Error")))
                }
            }else{
                emit(DataState.Response(UIComponent.None("No internet connection")))
            }

            val localPhotos = repository.getRecentPhotosLocal(
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
            Log.v("SearchGridVM", "finally idle")
        }
    }
}