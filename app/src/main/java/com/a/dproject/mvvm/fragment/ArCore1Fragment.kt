package com.a.dproject.mvvm.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentArCore1Binding
import com.a.dproject.mvvm.viewmodel.ArCore1ViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation


@ListFragmentAnnotation("arCore学习1", parentName = "ArCore")
class ArCore1Fragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentArCore1Binding
    lateinit var viewModel: ArCore1ViewModel

    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_ar_core1
    }

    override fun getStoneId(): Int {
        return 0
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)
        initViewModel()
        initView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initData()
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArCore1ViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id

    }


    private fun initObserver() {
    }


    private fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.onClickListener = this

    }

    private fun initData() {
    }


    override fun onClick(p0: View?) {

        p0?.let {
            when (it) {
                binding.message -> {
                    "message".toast()
                }
                binding.tvEvent -> {
                    "event".toast()
                }
                else -> {

                }
            }
        }

    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ArCore1Fragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
