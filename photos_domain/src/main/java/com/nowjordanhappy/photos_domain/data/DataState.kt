package com.nowjordanhappy.photos_domain.data

import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.core_ui.domain.UIComponent

sealed class DataState<T>{
    data class Response<T>(val uiComponent: UIComponent): DataState<T>()
    data class Data<T>(val data: T? = null): DataState<T>()
    data class Loading<T>(val progressBarState: ProgressBarState = ProgressBarState.Idle): DataState<T>()
}











