package com.nowjordanhappy.photos_ui.error

sealed class ErrorEvent{
    data class OnSetError(val message: String): ErrorEvent()
}
