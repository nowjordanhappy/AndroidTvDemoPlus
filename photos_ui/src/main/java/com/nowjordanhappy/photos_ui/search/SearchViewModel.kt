package com.nowjordanhappy.photos_ui.search

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowjordanhappy.core_ui.domain.UIComponent
import com.nowjordanhappy.photos_domain.use_case.SearchPhotos
import com.nowjordanhappy.photos_domain.data.DataState
import com.nowjordanhappy.photos_domain.use_case.PhotoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject constructor(
        private val photosUseCases: PhotoUseCases,
        private val connectivityManager: ConnectivityManager,
) : ViewModel(){
    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.OnChangeQuery -> {
                onChangeQuery(event)
            }
            SearchEvent.OnSearch -> {
                onSearch()
            }
            SearchEvent.OnNextPage -> {
                onNextPage()
            }
        }
    }

    private fun onChangeQuery(event: SearchEvent.OnChangeQuery){
        _state.value = _state.value.copy(
            query = event.query
        )
    }

    private fun onNextPage() {
        _state.value = _state.value.copy(
            page = _state.value.page + 1
        )
        onSearch()
    }

    private fun onSearch() {
        viewModelScope.launch {
            photosUseCases.searchPhotos.execute(
                query = _state.value.query,
                page = _state.value.page,
                pageSize = _state.value.pageSize,
                isNetworkAvailable = connectivityManager.isActiveNetworkMetered
            ).onEach { dataState ->
                when(dataState){
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            photos = dataState.data ?: emptyList()
                        )
                    }
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(
                            progressBarState = dataState.progressBarState
                        )
                    }
                    is DataState.Response -> {
                        when(dataState.uiComponent){
                            is UIComponent.None -> {

                            }
                        }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}