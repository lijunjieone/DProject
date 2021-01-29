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
import com.a.dproject.opengl.CircleSurfaceView
import com.a.dproject.opengl.CubeSurfaceViewV2
import com.a.dproject.opengl.SixStarSurfaceViewV2
import com.a.processor.ListFragmentAnnotation


@ListFragmentAnnotation("OpenGlV3 图形", parentName = "OpenGLV3Learn")
class GLShapeFragment : ArtBaseFragment() , View.OnClickListener{

    lateinit var binding: FragmentGlShapeBinding
    lateinit var viewModel: GLShapeViewModel

    lateinit var beltSurfaceView: BeltSurfaceView
    private lateinit var circleSurfaceView: CircleSurfaceView
    lateinit var sixStarSurfaceView: SixStarSurfaceViewV2
    lateinit var cubeSurfaceView: CubeSurfaceViewV2
    lateinit var cubeSurfaceView2: CubeSurfaceViewV2

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

        circleSurfaceView = CircleSurfaceView(requireContext())

        createCustomSurface()
        cubeSurfaceView = CubeSurfaceViewV2(requireContext())
        cubeSurfaceView2 = CubeSurfaceViewV2(requireContext())
        cubeSurfaceView2.setType(1)
    }
    private fun createCustomSurface() {
        sixStarSurfaceView = SixStarSurfaceViewV2(requireContext())
    }

//    private fun createCustomSurface() {
//        customSurfaceView = CustomSurfaceView(requireContext());
//        class CircleRenderer: CustomSurfaceView.SmallRenderer {
//            var circle //圆
//                    : CircleV3? = null
//           override fun onDrawFrame(gl: GL10){
//                //绘制圆
//                MatrixState.pushMatrix() //保护现场
//                MatrixState.translate(0f, 0.4f, 0f) //沿x轴正方向平移
//                circle!!.drawSelf(6, 12) //绘制半个圆
//                MatrixState.popMatrix() //恢复现场
//                //恢复现场
//                MatrixState.popMatrix()
//            }
//            override fun onSurfaceChanged(gl: GL10, width: Int, height: Int){}
//            override fun onSurfaceCreated(gl: GL10, config: EGLConfig){
//                //创建圆对象
//                circle = CircleV3(customSurfaceView)
//
//            }
//        }
//
//        customSurfaceView.initRender(CircleRenderer())
//    }



    private fun initData() {
    }



    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvBelt -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(beltSurfaceView)
                }
                binding.tvCircle -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(circleSurfaceView)
                }
                binding.tvCircle2 -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(sixStarSurfaceView)
                }
                binding.tvCube -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(cubeSurfaceView)
                }
                binding.tvCube2 -> {
                    binding.flContainer.removeAllViews()
                    binding.flContainer.addView(cubeSurfaceView2)
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
