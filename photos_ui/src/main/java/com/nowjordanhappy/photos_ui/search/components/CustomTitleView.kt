package com.nowjordanhappy.photos_ui.search.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import kotlin.jvm.JvmOverloads
import android.widget.RelativeLayout
import androidx.leanback.widget.TitleViewAdapter
import android.widget.TextView
import androidx.leanback.widget.SearchOrbView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.nowjordanhappy.photos_ui.R

class CustomTitleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle), TitleViewAdapter.Provider {
    private var mTitleView: TextView
    private var mSubtitleView: TextView
    private var mInformationView: TextView
    private var mGridModeView: ImageButton
    private lateinit var mSearchOrb: SearchOrbView

    private var gridModeOn = false
    private var gridModeHandler: ((Boolean) -> Unit)? = null

    private val mTitleViewAdapter: TitleViewAdapter = object : TitleViewAdapter() {
        override fun getSearchAffordanceView(): View {
            return mSearchOrb
        }

        override fun setTitle(titleText: CharSequence?) {
            this@CustomTitleView.setTitle(titleText)
        }

        override fun setOnSearchClickedListener(listener: OnClickListener?) {
            mSearchOrb.setOnClickListener(listener)
        }

        /*override fun getSearchAffordanceView(): View {
            return mSearchOrb
        }

        override fun setTitle(titleText: CharSequence) {
            this@CustomTitleView2.setTitle(titleText)
        }

        override fun setBadgeDrawable(drawable: Drawable) {
            //CustomTitleView.this.setBadgeDrawable(drawable);
        }

        override fun setOnSearchClickedListener(listener: OnClickListener) {
            mSearchOrb.setOnClickListener(listener)
        }

        override fun updateComponentsVisibility(flags: Int) {
            /*if ((flags & BRANDING_VIEW_VISIBLE) == BRANDING_VIEW_VISIBLE) {
                updateBadgeVisibility(true);
            } else {
                mAnalogClockView.setVisibility(View.GONE);
                mTitleView.setVisibility(View.GONE);
            }*/
            val visibility =
                if (flags and SEARCH_VIEW_VISIBLE == SEARCH_VIEW_VISIBLE) VISIBLE else INVISIBLE
            // mSearchOrbView.setVisibility(visibility);
        }

        private fun updateBadgeVisibility(visible: Boolean) {
            if (visible) {
                mTitleView.visibility = VISIBLE
            } else {
                mTitleView.visibility = GONE
            }
        }*/
    }

    fun setTitle(title: CharSequence?) {
        if (title != null) {
            mTitleView.text = title
            mTitleView.visibility = VISIBLE
        }
    }

    fun setSubtitle(subtitle: CharSequence?) {
        if (subtitle != null) {
            mSubtitleView.text = subtitle
            mSubtitleView.visibility = VISIBLE
        }
    }

    fun setInformation(information: CharSequence?) {
        if (information != null) {
            mInformationView.text = information
            mInformationView.visibility = VISIBLE
        }else{
            mInformationView.visibility = INVISIBLE
        }
    }

    fun setBadgeDrawable(drawable: Drawable?) {
        if (drawable != null) {
            //mTitleView.setVisibility(View.GONE);
        }
    }

    override fun getTitleViewAdapter(): TitleViewAdapter {
        return mTitleViewAdapter
    }

    fun setGridModeListener(gridModeHandler: ((Boolean) -> Unit)? = null) {
        this.gridModeHandler = gridModeHandler
    }

    private fun toggleIcon() {
        mGridModeView.setImageResource(if (gridModeOn) R.drawable.ic_grid_off else R.drawable.ic_grid_on)
    }

    fun hideActions(){
        mSubtitleView.visibility = View.INVISIBLE
        mGridModeView.visibility = View.INVISIBLE
        mSearchOrb.visibility = View.INVISIBLE
    }

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.custom_title_view, this)
        mTitleView = root.findViewById(R.id.title_tv)
        mSubtitleView = root.findViewById(R.id.subtitle_tv)
        mInformationView = root.findViewById(R.id.information_tv)
        mGridModeView = root.findViewById(R.id.grid_ib)
        mSearchOrb = root.findViewById(R.id.search_orb)

        mGridModeView.visibility = View.VISIBLE
        toggleIcon()

        mGridModeView.setOnClickListener {
            gridModeOn = !gridModeOn
            toggleIcon()
            gridModeHandler?.invoke(gridModeOn)
        }
    }
}