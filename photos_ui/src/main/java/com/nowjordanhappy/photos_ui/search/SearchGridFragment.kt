package com.nowjordanhappy.photos_ui.search

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.photos_domain.model.Photo
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
                viewModel.gridModeOn.collect{ gridModeOn->
                    updateGridMode(gridModeOn)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.photos.collect{ photos->
                    Log.v("SearchGridVM", "onRecentPhotos Fragment photos: ${photos.size}")
                    updateData(photos)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.progressBarState.collect{ progressBarState->
                    Log.v(TAG, "progressBarState: ${progressBarState}")
                    when(progressBarState){
                        ProgressBarState.Idle -> {
                            Log.v(TAG, "progressBarState Idle")
                            findNavController().popBackStack(destinationId = R.id.spinnerFragment, inclusive = true)
                        }
                        ProgressBarState.Loading -> {
                            Log.v(TAG, "progressBarState Loading")
                            findNavController().navigate(R.id.spinnerFragment)
                        }
                    }
                }
            }
        }
    }

    private fun updateData(photos: List<Photo>) {
        Log.v(TAG, "updateData")
        setInformation(isEmpty = photos.isEmpty())
        setupAdapterPhotos()
        photos.forEach {
            mAdapter?.add(it)
        }
    }

    private fun updateGridMode(gridModeOn: Boolean){
    }

    private fun setInformation(isEmpty: Boolean){
        myCustomTitleView?.setInformation(if(!isEmpty) null else requireContext().getString(com.nowjordanhappy.core_ui.R.string.empty_result_search))
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

        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                if (item is Photo) {
                    /*viewModel.selectPhoto = item

                    if (item.server != null && item.id != null && item.secret != null){
                        val intent = Intent(requireContext(), PhotoActivity::class.java)
                        intent.putExtra("server", item.server)
                        intent.putExtra("id", item.id)
                        intent.putExtra("secret", item.secret)
                        startActivity(intent)
                    }else{
                        Toast.makeText(requireContext(), "Some data is missing for the item", Toast.LENGTH_SHORT).show()
                    }*/
                }
            }

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is Photo) {
                mAdapter?.let { adat->
                    adat.indexOf(item).let { position ->
                        if(position != -1){
                            Log.v(TAG, "onItemClicked position: $position")
                            //tryToFetchData(position)
                            val mCurrentTotal = viewModel.photos.value.size
                            val lasPosition = kotlin.math.min(mCurrentTotal - 1, mCurrentTotal - MAX_LAST_RANGE - 1)

                            if(lasPosition > -1 && position >= lasPosition){
                                viewModel.onEvent(SearchEvent.OnNextPage)
                            }
                        }
                    }
                }
            }
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