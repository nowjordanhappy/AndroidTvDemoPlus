package com.nowjordanhappy.core_ui.utils

sealed class UiEvent{
    object Success: UiEvent()
    object NavigateUp: UiEvent()
    data class ShowToast(val message: String): UiEvent()
    data class ShowError(val message: String): UiEvent()
}