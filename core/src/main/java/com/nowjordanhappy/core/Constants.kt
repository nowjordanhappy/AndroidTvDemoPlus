package com.nowjordanhappy.core

object Constants {
    const val BASE_URL = "https://www.flickr.com/services/"
    const val IMAGE_BASE_URL = "https://live.staticflickr.com/"
    const val API_KEY = "23412e392c94e78fbd6398848503e847"

    const val RECIPE_PAGINATION_PAGE_SIZE = 30

    const val METHOD_GALLERY_GET_INFO = "flickr.galleries.getInfo"
    const val METHOD_SEARCH = "flickr.photos.search"
    const val METHOD_PHOTOS_GET_RECENT = "flickr.photos.getRecent"

    const val API_GALLERY_ID = "72157720697202794"
    const val API_GALLERY_LIMIT = 20
    const val API_GALLERY_SHORT_LIMIT = 10
    const val API_FORMAT = FormatResponseHelper.JSON
    const val API_NOJSONCALLBACK = NojsoncallbackHelper.ENABLE

    const val API_SEARCH_SORT_BY = SortSearchType.postedDesc
    const val API_SEARCH_PER_PAGE = 15
    const val API_PHOTO_EXTRAS = "owner_name,date_upload"
}