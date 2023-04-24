package com.nowjordanhappy.photos_ui.search

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import com.nowjordanhappy.core_ui.PermissionsHandler

class SearchFragment: SearchSupportFragment(),
    SearchSupportFragment.SearchResultProvider{

    private lateinit var mRowsAdapter: ArrayObjectAdapter

    private val viewModel by viewModels<SearchViewModel>()

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
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.onEvent(SearchEvent.OnChangeQuery(query ?: ""))

        return true
    }

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    companion object {
        private val TAG = SearchFragment::class.java.simpleName
        private const val REQUEST_SPEECH = 13
    }
}