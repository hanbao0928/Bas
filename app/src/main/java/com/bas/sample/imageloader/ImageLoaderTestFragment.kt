package com.bas.sample.imageloader

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import bas.droid.adapter.imageloader.load
import com.bas.R

/**
 * Created by Lucio on 2022/3/2.
 */
class ImageLoaderTestFragment() : Fragment(R.layout.imageloader_content_fragment) {

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.image_view).load("https://img2.baidu.com/it/u=1454430554,940955087&fm=253&fmt=auto&app=120&f=JPEG?w=640&h=456")
    }
}