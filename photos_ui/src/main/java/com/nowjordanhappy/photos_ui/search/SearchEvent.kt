package com.nowjordanhappy.photos_ui.search

sealed class SearchEvent{
    data class OnChangeQuery(val query: String): SearchEvent()
    object OnRecentPhotos: SearchEvent()
    object OnSearch: SearchEvent()
    object OnNextPage: SearchEvent()
    data class OnChangeGridMode(val gridModeOn: Boolean): SearchEvent()
}
