package com.nowjordanhappy.photos_ui.error

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nowjordanhappy.photos_ui.search.PhotoSelected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel(){

    private val args = ErrorFragmentArgs.fromSavedStateHandle(state) // This is null

    val message = state.get<String>("message")

    private val _messageError: MutableStateFlow<String> = MutableStateFlow("Unknown Error")
    val messageError = _messageError.asStateFlow()

    init {
        val message = state.get<String>("message") ?: "Unknown Error"
        Log.v("ErrorViewModel", "message: $message - new: ${args.message}")
    }

    fun onEvent(event: ErrorEvent.OnSetError){
        when(event){
          is ErrorEvent.OnSetError->{
              onSetError(event)
          }
        }
    }

    private fun onSetError(event: ErrorEvent.OnSetError) {
        _messageError.value = event.message
    }

}