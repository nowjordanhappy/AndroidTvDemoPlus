package com.nowjordanhappy.photos_ui.search

sealed class SearchEvent{
    data class OnChangeQuery(val query: String): SearchEvent()
    object OnSearch: SearchEvent()
    object OnNextPage: SearchEvent()
}
