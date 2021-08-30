package com.bas.android.tangram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2021/7/27.
 */
abstract class BaseTangramFragment : Fragment() {

    protected lateinit var recyclerView: RecyclerView

    @NonNull
    protected abstract fun createContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = createContentView(inflater, container, savedInstanceState)
        return contentView
    }
}