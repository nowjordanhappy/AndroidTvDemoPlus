package com.nowjordanhappy.photos_ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.core_ui.domain.UIComponent
import com.nowjordanhappy.photos_domain.data.DataState
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_domain.use_case.PhotoUseCases
import com.nowjordanhappy.photos_ui.utils.ManagerConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchGridViewModel
    @Inject constructor(
        private val photosUseCases: PhotoUseCases,
        private val connectivityManager: ManagerConnection,
) : ViewModel(){
    val numColumns = 3
    val maxListRange = 3
    val gridWidth = (ScreenHelper.getScreenWidth() * 0.95).toInt()
    val cartWidth = (0.8 * ScreenHelper.getScreenWidth() / numColumns).toInt()
    val cardHeight = (cartWidth * 0.6).toInt()

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _progressBarState: MutableStateFlow<ProgressBarState> = MutableStateFlow(ProgressBarState.Idle)
    val progressBarState: StateFlow<ProgressBarState> = _progressBarState.asStateFlow()

    private val _photoList = MutableStateFlow(PhotoListState())
    val photoList: StateFlow<PhotoListState> = _photoList.asStateFlow()

    /*private val _photos = MutableStateFlow(emptyList<Photo>())
    val photos: StateFlow<List<Photo>> = _photos.asStateFlow()*/

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _page = MutableStateFlow(1)
    //val page = _page.asStateFlow()

    private val _pageSize = MutableStateFlow(Constants.RECIPE_PAGINATION_PAGE_SIZE)
    //val pageSize = _pageSize.asStateFlow()

    /*private val _gridModeOn = MutableStateFlow(false)
    val gridModeOn = _gridModeOn.asStateFlow()*/

    private val _listSearchType: MutableStateFlow<ListSearchType> = MutableStateFlow(ListSearchType.Recent)
    val listSearchType = _listSearchType.asStateFlow()

    private val _selectedPhoto: MutableStateFlow<PhotoSelected> = MutableStateFlow(PhotoSelected())
    val selectedPhoto = _selectedPhoto.asStateFlow()

    private val _uiEvent = Channel<SearchGridUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        //onEvent(SearchEvent.OnRecentPhotos)
        loadMoreData()
    }

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.OnChangeQuery -> {
                onChangeQuery(event)
            }
            SearchEvent.OnSearch -> {
                onSearch()
            }
            SearchEvent.OnRecentPhotos -> {
                onRecentPhotos()
            }
            SearchEvent.OnNextPage -> {
                onNextPage()
            }
            is SearchEvent.OnChangeGridMode -> {
                onChangeGridMode(event)
            }
            is SearchEvent.OnSelectPhoto -> {
                onSelectPhoto(event)
            }
        }
    }

    private fun onSelectPhoto(event: SearchEvent.OnSelectPhoto) {
        val index = _photoList.value.photos.indexOf(event.photo)
        _selectedPhoto.value = _selectedPhoto.value.copy(
            photo = event.photo,
            index = index
        )
    }

    private fun onChangeGridMode(event: SearchEvent.OnChangeGridMode) {
        /*_state.value = _state.value.copy(
            gridModeOn = event.gridModeOn
        )*/
        //_gridModeOn.value = _gridModeOn.value
        _photoList.value = _photoList.value.copy(
            gridModeOn = event.gridModeOn
        )
    }

    private fun onChangeQuery(event: SearchEvent.OnChangeQuery){
        /*_state.value = _state.value.copy(
            query = event.query
        )*/
        _query.value = event.query
    }

    private fun onNextPage() {
        /*_state.value = _state.value.copy(
            page = _state.value.page + 1
        )*/
        _page.value = _page.value + 1
        loadMoreData()
    }

    private fun loadMoreData(){
        when(_listSearchType.value){
            ListSearchType.Recent -> {
                onRecentPhotos(isForNextPage = true)
            }
            ListSearchType.Search -> {
                onSearch(isForNextPage = true)
            }
        }
    }

    private fun onSearch(isForNextPage: Boolean = false) {
        viewModelScope.launch {
            photosUseCases.searchPhotos.execute(
                query = _state.value.query,
                page = _page.value,
                pageSize = _pageSize.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable()
            ).onEach { dataState ->
                when(dataState){
                    is DataState.Data -> {
                        /*_photoList.value = _photoList.value.copy(
                            photos = dataState.data ?: emptyList()
                        )*/
                        if(!isForNextPage){
                            _photoList.value = _photoList.value.copy(
                                photos = dataState.data ?: emptyList()
                            )

                            //_photos.value = dataState.data ?: emptyList()
                        }else{
                            appendPhotos(dataState.data ?: emptyList())
                            _uiEvent.send(SearchGridUiEvent.SuccessSearch)
                        }
                    }
                    is DataState.Loading -> {
                        _progressBarState.value = dataState.progressBarState
                    }
                    is DataState.Response -> {
                        when(dataState.uiComponent){
                            is UIComponent.None -> {
                                val message = (dataState.uiComponent as UIComponent.None).message
                                Log.v("SearchGridVM", "onSearch UIComponent.None: $message")
                                _uiEvent.send(SearchGridUiEvent.ShowError(message))
                            }
                        }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun onRecentPhotos(isForNextPage: Boolean = false) {
        Log.v("SearchGridVM", "onRecentPhotos")
        viewModelScope.launch {
            photosUseCases.getRecentPhotos.execute(
                page = _page.value,
                pageSize = _pageSize.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable()
            ).onEach { dataState ->
                when(dataState){
                    is DataState.Data -> {
                        //_photos.emit(dataState.data ?: emptyList())
                        if(!isForNextPage){
                            _photoList.value = _photoList.value.copy(
                                photos = dataState.data ?: emptyList()
                            )

                            //_photos.value = dataState.data ?: emptyList()
                        }else{
                            appendPhotos(dataState.data ?: emptyList())
                        }
                        Log.v("SearchGridVM", "onRecentPhotos updating data: ${_photoList.value.photos.size}")
                    }
                    is DataState.Loading -> {
                        Log.v("SearchGridVM", "onRecentPhotos progressBarState: ${dataState.progressBarState}")
                        _progressBarState.value = dataState.progressBarState
                    }
                    is DataState.Response -> {
                        when(dataState.uiComponent){
                            is UIComponent.None -> {
                                val message = (dataState.uiComponent as UIComponent.None).message
                                Log.v("SearchGridVM", "onRecentPhotos UIComponent.None: $message")
                                _progressBarState.value = ProgressBarState.Idle
                                _uiEvent.send(SearchGridUiEvent.ShowError(message))
                            }
                        }
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun appendPhotos(_photos: List<Photo>){
        val current = ArrayList(this._photoList.value.photos)
        current.addAll(_photos)
        this._photoList.value = this._photoList.value.copy(
            photos = current
        )
    }
}

sealed class SearchGridUiEvent{
    object SuccessSearch: SearchGridUiEvent()
    data class ShowError(val message: String): SearchGridUiEvent()
}