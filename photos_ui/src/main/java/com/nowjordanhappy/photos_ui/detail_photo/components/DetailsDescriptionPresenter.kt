package com.nowjordanhappy.photos_ui.detail_photo.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_ui.search.components.CardPresenter
import kotlin.properties.Delegates

class DetailsDescriptionPresenter(
    private val width: Int = 313,
    private val height: Int = 176,
) : Presenter(){
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        sDefaultBackgroundColor = ContextCompat.getColor(
            parent.context,
            com.nowjordanhappy.core_ui.R.color.default_background
        )
        sSelectedBackgroundColor =
            ContextCompat.getColor(
                parent.context,
                com.nowjordanhappy.core_ui.R.color.selected_background
            )
        mDefaultCardImage = ContextCompat.getDrawable(
            parent.context,
            com.nowjordanhappy.core_ui.R.drawable.no_image
        )

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val photo = item as Photo
        val cardView = viewHolder.view as ImageCardView

        if (photo.imageUrl != null) {
            cardView.minimumWidth = width

            Glide.with(cardView.context)
                .load(photo.imageUrl)
                .centerCrop()
                .placeholder(com.nowjordanhappy.core_ui.R.drawable.placeholder_background)
                .error(mDefaultCardImage)
                .into(cardView.mainImageView)

        } else {
            cardView.mainImageView.setImageDrawable(mDefaultCardImage)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        view.infoAreaBackground = null
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        view.setBackgroundColor(color)
    }
}