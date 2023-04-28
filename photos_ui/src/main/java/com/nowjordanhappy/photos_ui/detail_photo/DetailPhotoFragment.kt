package com.nowjordanhappy.photos_ui.detail_photo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.leanback.app.*
import androidx.leanback.widget.*
import androidx.leanback.widget.ListRow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.nowjordanhappy.core_ui.ScreenHelper
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.photos_domain.model.Photo
import com.nowjordanhappy.photos_ui.R
import com.nowjordanhappy.photos_ui.detail_photo.components.DetailsDescriptionPresenter
import com.nowjordanhappy.photos_ui.search.SearchEvent
import com.nowjordanhappy.photos_ui.search.SearchGridFragment
import com.nowjordanhappy.photos_ui.search.SearchGridViewModel
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Loads a grid of cards with movies to browse.
 */
class DetailPhotoFragment : DetailsSupportFragment() {
    private val viewModel: SearchGridViewModel by activityViewModels()

    private var myCustomTitleView: CustomTitleView? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mBackgroundManager: BackgroundManager

    private var mDefaultBackground: Drawable? = null

    private lateinit var mRowsAdapter: ArrayObjectAdapter

    var job: Job? = null
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.selectedPhoto.collect{ selectedPhoto->
                    initializeBackground(selectedPhoto.photo?.imageUrl)
                }
            }
        }

        /*
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

        adapter = mRowsAdapter*/

        //startUpdates()

        view?.isFocusableInTouchMode = true
        view?.requestFocus()

        view?.setOnKeyListener(object : OnKeyListener{
            override fun onKey(p0: View?, keyCode: Int, key: KeyEvent?): Boolean {
                Log.v("Detail", "fragment keyCode: $keyCode")

                if(key?.action == KeyEvent.ACTION_DOWN){
                    keyCode.let { keyEvent ->
                        viewModel.selectedPhoto.value.photo?.let {photo: Photo ->
                            val index = viewModel.selectedPhoto.value.index
                            when(keyEvent){
                                KeyEvent.KEYCODE_DPAD_LEFT->{
                                    //LEFT
                                    if(index > -1){
                                        viewModel.photoList.value.photos.getOrNull(index-1)?.let { previous->
                                            viewModel.onEvent(SearchEvent.OnSelectPhoto(previous))
                                        }
                                    }
                                    return true
                                }

                                KeyEvent.KEYCODE_DPAD_RIGHT->{
                                    //RIGHT
                                    if(index > -1){
                                        viewModel.photoList.value.photos.getOrNull(index+1)?.let { previous->
                                            viewModel.onEvent(SearchEvent.OnSelectPhoto(previous))
                                        }
                                    }
                                    return true
                                }
                                KeyEvent.META_SYM_ON->{
                                   findNavController().popBackStack()
                                    return true
                                }

                                else-> return true
                            }
                        }
                    }
                }
                return true
            }

        })
    }

    private fun startUpdates() {
        stopUpdates()
        job = viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                // this block is automatically executed when moving into
                // the started state, and cancelled when stopping.
                //while (true) {
                    viewModel.selectedPhoto.value.photo?.let { photo->

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

    private fun stopUpdates() {
        job?.cancel()
        job = null
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
        Log.v("Detailphoto", "initializeBackground: ${imageUrl}")
        mDetailsBackground.enableParallax()
        Glide.with(requireContext())
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
            })

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

    public fun rightAction(){
        //RIGHT
        viewModel.selectedPhoto.value.photo?.let { photo: Photo ->
            val index = viewModel.selectedPhoto.value.index
            if (index > -1) {
                viewModel.photoList.value.photos.getOrNull(index + 1)?.let { next ->
                    Log.v("Detail", "next: ${index + 1} - ${photo.dateUpload} - ${photo.title}")
                    viewModel.onEvent(SearchEvent.OnSelectPhoto(next))
                }
            }
        }
    }

    public fun leftAction(){
        //RIGHT
        viewModel.selectedPhoto.value.photo?.let { photo: Photo ->
            val index = viewModel.selectedPhoto.value.index
            if (index > -1) {
                viewModel.photoList.value.photos.getOrNull(index - 1)?.let { next ->
                    Log.v("Detail", "next: ${index + 1} - ${photo.dateUpload} - ${photo.title}")
                    viewModel.onEvent(SearchEvent.OnSelectPhoto(next))
                }
            }
        }
    }

    companion object {
        private val TAG = "DetailPhotoFragment"
    }
}
