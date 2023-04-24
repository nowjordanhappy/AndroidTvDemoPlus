package com.nowjordanhappy.photos_ui.spinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nowjordanhappy.photos_ui.R


class SpinnerFragment: DialogFragment() {
    private val TAG = SpinnerFragment::class.java.simpleName
    private val SPINNER_WIDTH = 100
    private val SPINNER_HEIGHT = 100

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*val progressBar = ProgressBar(container?.context)
        if (container is FrameLayout) {
            val layoutParams =
                FrameLayout.LayoutParams(SPINNER_WIDTH, SPINNER_HEIGHT, Gravity.CENTER)
            progressBar.layoutParams = layoutParams
        }
        return progressBar*/
        val view = inflater.inflate(
            R.layout.spinner_dialog,
            container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}