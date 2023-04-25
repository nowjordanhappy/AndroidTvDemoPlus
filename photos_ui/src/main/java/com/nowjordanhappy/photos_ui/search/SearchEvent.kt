package com.nowjordanhappy.photos_ui.search

import com.nowjordanhappy.photos_domain.model.Photo

sealed class SearchEvent{
    data class OnChangeQuery(val query: String): SearchEvent()
    object OnRecentPhotos: SearchEvent()
    object OnSearch: SearchEvent()
    object OnNextPage: SearchEvent()
    data class OnChangeGridMode(val gridModeOn: Boolean): SearchEvent()
    data class OnSelectPhoto(val photo: Photo): SearchEvent()
}
