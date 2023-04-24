package com.nowjordanhappy.photos_ui.search.components

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.updateLayoutParams
import androidx.leanback.widget.Presenter

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardLoadingPresenter(
    private val width: Int = 313,
    private val height: Int = 176
) : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {

        val cardView = LayoutInflater.from(parent.context)
            .inflate(com.nowjordanhappy.photos_ui.R.layout.card_loading_layout, parent, false);

        cardView.isFocusable = false
        cardView.isFocusableInTouchMode = false
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val item = item as? Int
        val cardView = viewHolder.view as RelativeLayout
        cardView.updateLayoutParams {
            height = this@CardLoadingPresenter.height
            width = this@CardLoadingPresenter.width
        }

        /*val shimmer = cardView.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        if(!shimmer.isShimmerVisible)shimmer.startShimmer()*/
        Log.d(TAG, "onBindViewHolder")
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as RelativeLayout
        /*val shimmer = cardView.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        if(shimmer.isShimmerVisible)shimmer.stopShimmer()*/
    }

    companion object {
        private val TAG = "CardLoadingPresenter"
    }
}