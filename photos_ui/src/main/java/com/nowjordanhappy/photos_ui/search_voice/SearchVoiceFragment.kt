package com.nowjordanhappy.photos_ui.search_voice

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import androidx.navigation.fragment.findNavController
import com.nowjordanhappy.core_ui.PermissionsHandler
import com.nowjordanhappy.photos_ui.search.SearchEvent
import com.nowjordanhappy.photos_ui.search.SearchGridViewModel

class SearchVoiceFragment: SearchSupportFragment(),
    SearchSupportFragment.SearchResultProvider{

    private lateinit var mRowsAdapter: ArrayObjectAdapter

    private val viewModel by viewModels<SearchVoiceViewModel>()

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data->
                setSearchQuery(data, true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setParams()
    }

    private fun setParams(){
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)

        if (!PermissionsHandler.hasPermission(requireActivity(), Manifest.permission.RECORD_AUDIO)) {
            setSpeechRecognitionCallback {
                Log.v(TAG, "recognizeSpeech")
                try {
                    openSpeechRecognitionForResult()
                } catch (e: ActivityNotFoundException) {
                    Log.e(
                        TAG,
                        "Cannot find activity for speech recognizer",
                        e
                    )
                }
            }
        }
    }

    private fun openSpeechRecognitionForResult(){
        resultLauncher.launch(recognizerIntent)
    }

    override fun getResultsAdapter(): ObjectAdapter {
        return mRowsAdapter
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        Log.v("SearchVoice", "onQueryTextChange: $newQuery")
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.v("SearchVoice", "onQueryTextSubmit: $query")
        viewModel.onEvent(SearchVoiceEvent.OnChangeQuery(query ?: ""))

        if(!query.isNullOrBlank()){
            setFragmentResult(
                "searchVoice",
                bundleOf("query" to query)
            )
            findNavController().popBackStack()
        }

        return true
    }


    companion object {
        private val TAG = SearchVoiceFragment::class.java.simpleName
    }
}