package com.nowjordanhappy.core_ui.domain

sealed class UIComponent{

    //For logging or sending analytics
    data class None(
        val message: String
    ): UIComponent()
}
