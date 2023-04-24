package com.nowjordanhappy.photos_ui.search

import com.nowjordanhappy.core.Constants
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.photos_domain.model.Photo

data class SearchState (
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val photos: List<Photo> = emptyList(),
    val query: String = "",
    val page: Int = 1,
    val pageSize: Int = Constants.RECIPE_PAGINATION_PAGE_SIZE,
)