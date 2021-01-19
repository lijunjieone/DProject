package com.a.dproject.mvvm.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentGlFirstBinding
import com.a.dproject.mvvm.viewmodel.GlFirstViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation


@ListFragmentAnnotation("OpenGlV3 入门", parentName = "OpenGLV3Learn")
class GlFirstFragment : ArtBaseFragment() , View.OnClickListener{

    protected lateinit var binding: FragmentGlFirstBinding
    lateinit var viewModel: GlFirstViewModel

    var id:Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_gl_first
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
        viewModel = ViewModelProviders.of(this).get(GlFirstViewModel::class.java)
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
                else ->{


            }
            }
        }
    }

        
    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = GlFirstFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }



}
