package com.nowjordanhappy.core_ui

import android.content.res.Resources

object ScreenHelper {
    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

}