package com.bas.sample.leanbacktab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import bas.android.core.util.Logger
import com.bas.R

/**
 * Created by Lucio on 2021/10/12.
 */
class TabFragment() : Fragment(R.layout.leanback_tab_fragment) {

    init {
        Logger.d("TabActivity","创建TabFragment${this.hashCode()}")
    }
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
        view.findViewById<View>(R.id.right_btn).setOnFocusChangeListener { v, hasFocus ->
            Logger.d("TabActivity","hasFocus=$hasFocus $v ")
        }
    }
}