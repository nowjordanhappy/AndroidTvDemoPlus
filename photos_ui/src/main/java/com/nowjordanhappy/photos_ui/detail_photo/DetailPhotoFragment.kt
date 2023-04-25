package com.nowjordanhappy.photos_ui.detail_photo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.leanback.app.*
import androidx.leanback.widget.*
import androidx.leanback.widget.ListRow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_ui.detail_photo.components.DetailsDescriptionPresenter
import com.nowjordanhappy.photos_ui.search.SearchGridViewModel
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView
import kotlinx.coroutines.launch


/**
 * Loads a grid of cards with movies to browse.
 */
class DetailPhotoFragment : DetailsSupportFragment(), OnItemViewClickedListener {
    private val viewModel by activityViewModels<SearchGridViewModel>()

    private var myCustomTitleView: CustomTitleView? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mBackgroundManager: BackgroundManager

    private var mDefaultBackground: Drawable? = null

    private lateinit var mRowsAdapter: ArrayObjectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)
        mDetailsBackground.enableParallax()
        prepareBackgroundManager()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        setupCustomTitleView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setParams()
        setupEventListeners()
    }

    private fun setParams(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.selectedPhoto.collect{ selectedPhoto->
                    initializeBackground(selectedPhoto?.imageUrl)
                }
            }
        }

        val rowPresenter: FullWidthDetailsOverviewRowPresenter =
            object : FullWidthDetailsOverviewRowPresenter(
                DetailsDescriptionPresenter(width = ScreenHelper.getScreenWidth(), height = ScreenHelper.getScreenHeight())
            ) {
                override fun createRowViewHolder(parent: ViewGroup?): RowPresenter.ViewHolder? {
                    // Customize Actionbar and Content by using custom colors.
                    val viewHolder = super.createRowViewHolder(parent)
                    /*val actionsView =
                        viewHolder.view.findViewById<View>(R.id.details_overview_actions_background)
                    actionsView.setBackgroundColor(activity!!.resources.getColor(R.color.detail_view_actionbar_background))
                    val detailsView = viewHolder.view.findViewById<View>(R.id.details_frame)
                    detailsView.setBackgroundColor(
                        resources.getColor(com.nowjordanhappy.core_ui.R.color.search_background)
                    )*/
                    return viewHolder
                }
            }

        val shadowDisabledRowPresenter = ListRowPresenter()
        shadowDisabledRowPresenter.shadowEnabled = false

        val rowPresenterSelector = ClassPresenterSelector()
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, rowPresenter)
        //rowPresenterSelector.addClassPresenter(CardListRow::class.java, shadowDisabledRowPresenter)
        rowPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        mRowsAdapter = ArrayObjectAdapter(rowPresenterSelector)

        // Setup action and detail row.
        // Setup action and detail row.
        val detailsOverview = DetailsOverviewRow(viewModel.selectedPhoto.value)
        Glide.with(requireContext())
            .asBitmap()
            .centerCrop()
            .error(com.nowjordanhappy.core_ui.R.drawable.default_background)
            .load(viewModel.selectedPhoto.value?.imageUrl)
            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    detailsOverview.setImageBitmap(requireContext(), bitmap)
                    //mBackgroundManager.setBitmap(bitmap)
                }
            })

        val actionAdapter = ArrayObjectAdapter()

        detailsOverview.actionsAdapter = actionAdapter;
        mRowsAdapter.add(detailsOverview);

        adapter = mRowsAdapter
    }

    private fun setupCustomTitleView(){
        (titleView as? CustomTitleView)?.let { myTitle->
            myCustomTitleView = myTitle
        }

        myCustomTitleView?.hideActions()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(requireActivity())
        //mBackgroundManager.attach(requireActivity().window)
    }

    private fun initializeBackground(imageUrl: String?) {
        Log.v("Detailphoto", "initializeBackground")
        mDetailsBackground.enableParallax()
        /*Glide.with(requireContext())
            .asBitmap()
            .centerCrop()
            .error(com.nowjordanhappy.core_ui.R.drawable.default_background)
            .load(imageUrl)
            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    mBackgroundManager.setBitmap(bitmap)
                }
            })*/

    }

    private fun setupEventListeners() {
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is Photo) {
                /*mAdapter?.let { adat->
                    adat.indexOf(item).let { position ->
                        if(position != -1){
                            Log.v(SearchGridFragment.TAG, "onItemClicked position: $position")
                            //tryToFetchData(position)
                            val currentTotal = viewModel.photoList.value.photos.size
                            val lasPosition = kotlin.math.min(currentTotal - 1, currentTotal - viewModel.maxListRange - 1)

                            if(lasPosition > -1 && position >= lasPosition){
                                viewModel.onEvent(SearchEvent.OnNextPage)
                            }
                        }
                    }
                }*/
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    companion object {
        private val TAG = "DetailPhotoFragment"
    }

    override fun onItemClicked(
        itemViewHolder: Presenter.ViewHolder?,
        item: Any?,
        rowViewHolder: RowPresenter.ViewHolder?,
        row: Row?
    ) {

    }
}
