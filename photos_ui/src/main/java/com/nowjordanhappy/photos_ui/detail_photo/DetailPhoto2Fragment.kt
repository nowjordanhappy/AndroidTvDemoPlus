package com.nowjordanhappy.photos_ui.detail_photo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.photos_ui.detail_photo.components.DetailsDescriptionPresenter
import com.nowjordanhappy.photos_ui.search.SearchGridViewModel
import com.nowjordanhappy.photos_ui.search.components.CardPresenter
import com.nowjordanhappy.photos_ui.search.components.CustomVerticalGridPresenter
import kotlinx.coroutines.launch

class DetailPhoto2Fragment: DetailsSupportFragment() {
    private val viewModel: SearchGridViewModel by activityViewModels()

    private var mAdapter: ArrayObjectAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {

        setupAdapterPhotos()
    }

    private fun setupAdapterPhotos(){

        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.setOnActionClickedListener {
            Log.v("Detail", "setOnActionClickedListener FullWidthDetailsOverviewRowPresenter")
        }
        adapter = ArrayObjectAdapter(detailsPresenter)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // this block is automatically executed when moving into
                // the started state, and cancelled when stopping.
                //while (true) {
                    viewModel.selectedPhoto.value.photo?.let { photo->
                        //if(adapter != null)adapter = null
                        setupAdapterPhotos()
                        mAdapter?.add(photo)
                        val index = viewModel.selectedPhoto.value.index
                        Log.v("Detail", "current index selected: $index")
                        if(index > -1){
                            viewModel.photoList.value.photos.getOrNull(index+1)?.let { next->
                                Log.v("Detail", "next: ${index+1} - ${photo.dateUpload} - ${photo.title}")
                                //viewModel.onEvent(SearchEvent.OnSelectPhoto(next))
                            }
                        }
                    }
                    //delay(2000)
                //}
            }
        }
    }
}