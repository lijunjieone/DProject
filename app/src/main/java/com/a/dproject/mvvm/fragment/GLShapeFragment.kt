package com.a.dproject.mvvm.fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentGlShapeBinding
import com.a.dproject.mvvm.viewmodel.GLShapeViewModel
import com.a.dproject.opengl.BeltSurfaceView
import com.a.processor.ListFragmentAnnotation


@ListFragmentAnnotation("OpenGlV3 图形", parentName = "OpenGLV3Learn")
class GLShapeFragment : ArtBaseFragment() , View.OnClickListener{

    lateinit var binding: FragmentGlShapeBinding
    lateinit var viewModel: GLShapeViewModel

    lateinit var beltSurfaceView: BeltSurfaceView

    var id:Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_gl_shape
    }

    override fun getStoneId(): Int {
        return 0
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)
        initViewModel()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
        initData()
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(GLShapeViewModel::class.java)
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


        beltSurfaceView = BeltSurfaceView(requireContext())
        binding.flContainer.addView(beltSurfaceView)

    }



    private fun initData() {
    }



    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvBelt -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(beltSurfaceView)
                }


                else -> {

                }
            }
        }
    }

        
    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = GLShapeFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }



}
