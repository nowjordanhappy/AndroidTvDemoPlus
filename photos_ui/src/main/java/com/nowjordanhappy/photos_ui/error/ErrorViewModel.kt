package com.nowjordanhappy.photos_ui.error

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel(){

    init {
        val message = state.get<String>("message") ?: "Unknown Error"
        Log.v("ErrorViewModel", "message: $message")
    }
}