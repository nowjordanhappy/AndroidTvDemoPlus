package com.nowjordanhappy.photos_ui.search_voice

import androidx.lifecycle.ViewModel
import com.nowjordanhappy.photos_ui.search.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchVoiceViewModel
@Inject constructor(
) : ViewModel(){
    private val _state = MutableStateFlow(SearchVoiceState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchVoiceEvent) {
        when (event) {
            is SearchVoiceEvent.OnChangeQuery -> {
                onChangeQuery(event)
            }
        }
    }

    private fun onChangeQuery(event: SearchVoiceEvent.OnChangeQuery){
        _state.value = _state.value.copy(
            query = event.query
        )
    }
}