package com.nowjordanhappy.photos_ui.search.components

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.BaseCardView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.nowjordanhappy.photos_domain.model.Photo

import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter(
    private val width: Int = 313,
    private val height: Int = 176,
    private val gridModeOn: Boolean
) : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    private var defaultDateString = "No Date"
    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")
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

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val photo = item as Photo
        val cardView = viewHolder.view as ImageCardView
        val titleTV = cardView.findViewById<TextView>(androidx.leanback.R.id.title_text)
        val contentTV = cardView.findViewById<TextView>(androidx.leanback.R.id.content_text)

        contentTV.setTextColor(Color.WHITE)

        cardView.cardType = BaseCardView.CARD_TYPE_INFO_OVER;
        Log.d(TAG, "onBindViewHolder")

        val date = photo.dateUpload
        cardView.titleText = photo.title
        cardView.contentText = "${photo.ownername ?: "Anonymous owner"} / $date"
        cardView.setMainImageDimensions(width, height)

        if (photo.imageUrl != null) {

            //Log.d(TAG, "onBindViewHolder url: $url - isSizeSetted: ${photo.isSizeSetted}")

            cardView.minimumWidth = width

            if(gridModeOn){
                Glide.with(cardView.context)
                    .load(photo.imageUrl)
                    .centerCrop()
                    .placeholder(com.nowjordanhappy.core_ui.R.drawable.placeholder_background)
                    .error(mDefaultCardImage)
                    .into(cardView.mainImageView)
            }else{
                Glide.with(cardView.context)
                    .asBitmap()
                    .placeholder(com.nowjordanhappy.core_ui.R.drawable.placeholder_background)
                    .load(photo.imageUrl)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            bitmap: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            Log.v(
                                TAG,
                                "viewholder width: ${bitmap.width} - height: ${bitmap.height}"
                            )
                            cardView.setMainImage(
                                BitmapDrawable(
                                    cardView.context.resources,
                                    bitmap
                                ), true
                            )

                            val newWidth = bitmap.width
                            val newHeight = bitmap.height
                            photo.isSizeSetted
                            Log.v(TAG, "viewholder width: ${newWidth} - height: ${newHeight}")
                            //cardView.setMainImageDimensions(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                            cardView.setMainImageDimensions(
                                width,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            cardView.setMainImage(mDefaultCardImage, true)
                        }
                    })
            }

        } else {
            cardView.mainImageView.setImageDrawable(mDefaultCardImage)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        view.infoAreaBackground = null
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        //view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val TAG = "CardPresenter"
    }

}