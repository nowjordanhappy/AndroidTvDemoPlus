package com.nowjordanhappy.photos_ui.search.components

import android.view.ViewGroup
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.VerticalGridPresenter

class CustomVerticalGridPresenter(
    private val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
) : VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_SMALL) {


    override fun initializeGridViewHolder(vh: ViewHolder?) {
        super.initializeGridViewHolder(vh)
        vh?.gridView?.let { gridView ->
            val params = gridView.layoutParams
            params.width = width
            gridView.layoutParams = params
        }
    }
}