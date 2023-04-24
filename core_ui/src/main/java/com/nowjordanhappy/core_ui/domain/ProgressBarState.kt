package com.nowjordanhappy.core_ui.domain

sealed class ProgressBarState{
    object Loading: ProgressBarState()
    object Idle: ProgressBarState()
}
