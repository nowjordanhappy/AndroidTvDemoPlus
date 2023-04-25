package com.nowjordanhappy.photos_ui.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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
import androidx.recyclerview.widget.RecyclerView
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_ui.R
import com.nowjordanhappy.photos_ui.search.components.CardPresenter
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView
import com.nowjordanhappy.photos_ui.search.components.CustomVerticalGridPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchGridFragment: VerticalGridSupportFragment() {
    private val viewModel by activityViewModels<SearchGridViewModel>()

    private var mAdapter: ArrayObjectAdapter? = null

    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null

    private var myCustomTitleView: CustomTitleView? = null

    private var isOnPause = false
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
                viewModel.uiEvent.collect{ uiEvent->
                    when(uiEvent){
                        is SearchGridUiEvent.ShowError -> {
                            Log.v(TAG, "ShowError: ${uiEvent.message}")
                            showError(uiEvent.message)
                        }
                        SearchGridUiEvent.SuccessSearch -> {
                            updateSubtitle(String.format(getString(com.nowjordanhappy.core_ui.R.string.format_search_query), viewModel.query.value))
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.photoList.collect{ photoList->
                    Log.v("SearchGridVM", "onRecentPhotos Fragment photos: ${photoList.photos.size} - gridOn: ${photoList.gridModeOn}")
                    //Reload data
                    mAdapter = null
                    updateData(photoList.photos)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.selectedPhoto.collect{ selectedPhoto->
                    selectedPhoto.photo?.let { photo ->
                        Log.v(TAG, "index: ${selectedPhoto.index} - ${photo.dateUpload} - ${photo.title} - isOnPause: $isOnPause")
                        if(selectedPhoto.index > -1){
                            if(isOnPause){
                                isOnPause = false
                                Log.v(TAG, "before setSelectedPosition")
                                //setSelectedPosition(selectedPhoto.index)
                                Log.v(TAG, "after setSelectedPosition: ${selectedPhoto.index}")
                            }
                            //adapter.getPresenter(photo)
                            //mGridViewHolder.gridView.getChildAt(index).requestFocus()
                            //adapter.presenterSelector.getPresenter(photo)
                            //setSelectedPosition(index)
                            /*val rowViewHolder = getRowViewHolder(rowIndex)
                            val horizontalGridView = (rowViewHolder as ListRowPresenter.ViewHolder).gridView
                            horizontalGridView.setSelectedPosition(cardPos)*/
                        }
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        //findNavController().navigate(R.id.errorFragment, bundleOf("message" to message), )
        //findNavController().navigate(R.id.errorFragment, bundleOf("message" to message), )
        val action = SearchGridFragmentDirections.actionSearchGridFragmentToErrorFragment(message+"12")
        findNavController().navigate(action)
    }

    private fun updateSubtitle(subtitle: String){
        myCustomTitleView?.setSubtitle(subtitle)
    }

    private fun updateData(photos: List<Photo>) {
        Log.v(TAG, "updateData")
        setInformation(isEmpty = photos.isEmpty())
        setupAdapterPhotos()
        photos.forEach {
            mAdapter?.add(it)
        }
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
            width = viewModel.gridWidth
        )
        gridPresenter.numberOfColumns = viewModel.numColumns
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
            //mAdapter = ArrayObjectAdapter(CardPresenter(cartWidth, cardHeight, gridModeOn = false))//viewModel.state.value.gridModeOn))
            mAdapter = ArrayObjectAdapter(CardPresenter(viewModel.cartWidth, viewModel.cardHeight, gridModeOn = viewModel.photoList.value.gridModeOn))
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
                    //viewModel.onEvent(SearchEvent.OnSelectPhoto(item))
                    Log.v(TAG, "OnItemViewClickedListener: ${item.dateUpload} - ${item.title} - isOnPause: $isOnPause")
                    findNavController().navigate(R.id.detailPhotoFragment)
                    isOnPause = true
                }
            }


        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is Photo) {
                Log.v(TAG, "setOnItemViewSelectedListener: ${item.dateUpload} - ${item.title} - isOnPause: $isOnPause")
                mAdapter?.let { adat->
                    viewModel.onEvent(SearchEvent.OnSelectPhoto(item))
                    //setSelectedPosition()
                    adat.indexOf(item).let { position ->
                        if(position != -1){
                            Log.v(TAG, "onItemClicked position: $position")
                            //tryToFetchData(position)
                            val currentTotal = viewModel.photoList.value.photos.size
                            val lasPosition = kotlin.math.min(currentTotal - 1, currentTotal - viewModel.maxListRange - 1)

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
    }
}