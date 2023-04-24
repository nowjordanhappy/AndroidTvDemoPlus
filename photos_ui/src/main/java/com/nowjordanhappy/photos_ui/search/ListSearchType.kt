package com.nowjordanhappy.photos_ui.search

sealed class ListSearchType{
    object Search: ListSearchType()
    object Recent: ListSearchType()
}