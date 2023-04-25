package com.nowjordanhappy.photos_ui.error

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.leanback.app.ErrorSupportFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nowjordanhappy.core_ui.domain.ProgressBarState
import com.nowjordanhappy.photos_ui.R
import com.nowjordanhappy.photos_ui.search.SearchGridFragment
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * This class demonstrates how to extend [ErrorSupportFragment].
 */
@AndroidEntryPoint
class ErrorFragment : ErrorSupportFragment() {
    private val viewModel by activityViewModels<ErrorViewModel>()

    private var myCustomTitleView: CustomTitleView? = null

    val args: ErrorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(com.nowjordanhappy.core_ui.R.string.app_name)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("ErrorFragment", "args.message: ${args.message} - vm: ${viewModel.message}")
        viewModel.onEvent(ErrorEvent.OnSetError(args.message))
        setParams()
    }

    private fun setParams(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.messageError.collect{ messageError->
                    setErrorMessage(messageError)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setErrorContent()

        setupCustomTitleView()

        setupUIElements()
    }

    private fun setErrorContent() {
        imageDrawable =
            ContextCompat.getDrawable(requireActivity(), androidx.leanback.R.drawable.lb_ic_sad_cloud)

        setDefaultBackground(TRANSLUCENT)

        buttonText = resources.getString(com.nowjordanhappy.core_ui.R.string.dismiss_error)
        buttonClickListener = View.OnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupUIElements() {
        title = getString(com.nowjordanhappy.core_ui.R.string.browse_title)
    }

    private fun setupCustomTitleView(){
        (titleView as? CustomTitleView)?.let { myTitle->
            //Log.v("ErrorFragment", "myTitle: $myTitle")
            myCustomTitleView = myTitle
        }

        myCustomTitleView?.hideActions()
    }

    fun setErrorMessage(message: String){
        this.message = message
    }

    companion object {
        private val TRANSLUCENT = true
    }
}