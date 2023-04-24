package com.nowjordanhappy.photos.domain.data

import com.nowjordanhappy.domain.ProgressBarState
import com.nowjordanhappy.domain.UIComponent

sealed class DataState<T>{
    data class Response<T>(val uiComponent: com.nowjordanhappy.domain.UIComponent): DataState<T>()
    data class Data<T>(val data: T? = null): DataState<T>()
    data class Loading<T>(val progressBarState: com.nowjordanhappy.domain.ProgressBarState = com.nowjordanhappy.domain.ProgressBarState.Idle): DataState<T>()
}











