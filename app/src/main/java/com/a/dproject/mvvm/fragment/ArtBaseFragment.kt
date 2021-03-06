package com.a.dproject.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment


open class ArtBaseFragment : Fragment() {

    protected lateinit var rootView: FrameLayout
    protected lateinit var contentView: View
    protected var stoneView: ViewGroup? = null
    protected var loadingView: ViewGroup? = null


    private var loadingType: Boolean = true


    companion object {

        const val SHIMMER_DURATION = 1800L

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = FrameLayout(container!!.context)
        if (getContentId() != 0) {
            contentView = inflater.inflate(getContentId(), rootView, false)
            rootView.addView(contentView)
        } else {
            contentView = FrameLayout(container!!.context)
            rootView.addView(contentView)
        }

        //设置loading的视图，getLoading,getStoneItemId,getStoneId 三个接口互斥
        if (getLoadingId() != 0) {
            loadingView = inflater.inflate(getLoadingId(), rootView, false) as ViewGroup
            rootView.addView(loadingView)

            loadingType = false
        }


        loadingView?.visibility = View.GONE
        stoneView?.visibility = View.GONE

        return rootView
    }


    fun setLoadingType(type: Boolean) {
        loadingType = type
    }

    open fun getContentId(): Int {
        return 0
    }

    open fun getStoneId(): Int {
        return 0
    }

    open fun getStoneItemId(): Int {
        return 0
    }

    open fun getLoadingId(): Int {
        return 0
    }


}