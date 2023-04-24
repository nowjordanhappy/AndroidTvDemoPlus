package com.nowjordanhappy.photos_ui.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.photos_ui.R
import com.nowjordanhappy.photos_ui.search.components.CardPresenter
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView
import com.nowjordanhappy.photos_ui.search.components.CustomVerticalGridPresenter
import kotlinx.coroutines.launch

class SearchGridFragment: VerticalGridSupportFragment() {
    private val viewModel by activityViewModels<SearchGridViewModel>()

    private var mAdapter: ArrayObjectAdapter? = null

    private val gridWidth = (ScreenHelper.getScreenWidth() * 0.95).toInt()
    private val cartWidth = (0.8 * ScreenHelper.getScreenWidth() / NUM_COLUMNS).toInt()
    private val cardHeight = (cartWidth * 0.6).toInt()

    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null

    private var myCustomTitleView: CustomTitleView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragment()
        prepareBackgroundManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setParams()
        setupCustomTitleView()
        setupEventListeners()
    }

    private fun setParams(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.state.collect{ state->
                    updateGridMode(state.gridModeOn)
                }
            }
        }
    }

    private fun updateGridMode(gridModeOn: Boolean){
    }

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground = ContextCompat.getDrawable(requireActivity(), com.nowjordanhappy.core_ui.R.drawable.search_background)
    }

    private fun setupCustomTitleView(){
        (titleView as? CustomTitleView)?.let { myTitle->
            myCustomTitleView = myTitle
        }

        Log.v("TAG", "setupCustomTitleView == null: ${myCustomTitleView == null}")
        myCustomTitleView?.setGridModeListener { gridModeOn ->
            viewModel.onEvent(SearchEvent.OnChangeGridMode(gridModeOn))
        }
    }

    private fun setupFragment() {

        val gridPresenter = CustomVerticalGridPresenter(
            width = gridWidth
        )
        gridPresenter.numberOfColumns = NUM_COLUMNS
        gridPresenter.shadowEnabled = true
        setGridPresenter(gridPresenter)

        setupAdapterPhotos()
    }

    private fun setupAdapterPhotos(){
        var shouldInitializeAdapter = true
        mAdapter?.let { it ->
            it.presenterSelector.presenters.firstOrNull()?.let {pres->
                shouldInitializeAdapter = pres !is CardPresenter
            }
        }

        if(shouldInitializeAdapter){
            mAdapter = ArrayObjectAdapter(CardPresenter(cartWidth, cardHeight, gridModeOn = viewModel.state.value.gridModeOn))
            adapter = mAdapter
        }
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            setFragmentResultListener("searchVoice"){ key, bundle->
                clearFragmentResultListener(requestKey = "searchVoice")
                val query = bundle.getString("query") ?: ""
                Log.v(TAG, "query voice: $query")
                viewModel.onEvent(SearchEvent.OnChangeQuery(query))
                viewModel.onEvent(SearchEvent.OnSearch)
            }

            findNavController().navigate(R.id.searchFragment)
        }
    }

    companion object {
        private val TAG = SearchGridFragment::class.java.simpleName
        private const val NUM_COLUMNS = 3
        private const val REQUEST_SEARCH = 13
        private const val REQUEST_ERROR = 14
        private const val MAX_LAST_RANGE = 3 //Get in last 3 items
        private const val LOADING_ITEMS_COUNT = 6
        private const val IS_SIZE_ITEM_FIXED = false
    }
}