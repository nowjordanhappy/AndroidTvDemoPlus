package com.nowjordanhappy.photos_ui.error

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.core.content.ContextCompat
import androidx.leanback.app.ErrorSupportFragment
import androidx.navigation.fragment.findNavController
import com.nowjordanhappy.photos_ui.search.components.CustomTitleView

/**
 * This class demonstrates how to extend [ErrorSupportFragment].
 */
class ErrorFragment : ErrorSupportFragment() {

    private var myCustomTitleView: CustomTitleView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(com.nowjordanhappy.core_ui.R.string.app_name)
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
            Log.v("ErrorFragment", "myTitle: $myTitle")
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