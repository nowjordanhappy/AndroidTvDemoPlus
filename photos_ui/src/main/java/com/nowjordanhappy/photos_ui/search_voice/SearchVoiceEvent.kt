package com.nowjordanhappy.photos_ui.search_voice

sealed class SearchVoiceEvent{
    data class OnChangeQuery(val query: String): SearchVoiceEvent()
}
