package com.a.dproject.mvvm.fragment


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.utils.Stroke
import com.a.dproject.databinding.FragmentDrawLineBinding
import com.a.dproject.mvvm.viewmodel.DrawLineViewModel
import com.a.dproject.showFragment
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.*
import com.google.ar.sceneform.Scene.OnPeekTouchListener
import com.google.ar.sceneform.Scene.OnUpdateListener
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.ux.ArFragment
import java.util.*


@ListFragmentAnnotation("arCore绘制图形", parentName = "ArCore")
class DrawLineFragment : ArtBaseFragment(), View.OnClickListener, OnUpdateListener, OnPeekTouchListener {

    protected lateinit var binding: FragmentDrawLineBinding
    lateinit var viewModel: DrawLineViewModel
    lateinit var material: Material
    lateinit var currentStroke: Stroke
    lateinit var arFragment: ArFragment
    lateinit var anchorNode: AnchorNode
    lateinit var andyRenderable: ModelRenderable
    private val strokes = ArrayList<Stroke>()

    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_draw_line
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
        viewModel = ViewModelProviders.of(this).get(DrawLineViewModel::class.java)
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

        setColor(WHITE)


        arFragment = ArFragment()

        R.id.arFragment.showFragment(arFragment, childFragmentManager)


    }

    override fun onResume() {
        super.onResume()
        arFragment.getArSceneView().getPlaneRenderer().setEnabled(false)
        arFragment.getArSceneView().getScene().addOnUpdateListener(this)
        arFragment.getArSceneView().getScene().addOnPeekTouchListener(this)
    }

    private fun setTexture(resourceId: Int) {
        Texture.builder()
                .setSource(requireContext(), resourceId)
                .setSampler(Texture.Sampler.builder().setWrapMode(Texture.Sampler.WrapMode.REPEAT).build())
                .build()
                .thenCompose { texture: Texture? -> MaterialFactory.makeOpaqueWithTexture(requireContext(), texture) }
                .thenAccept { material1: com.google.ar.sceneform.rendering.Material -> material = material1.makeCopy() }
//                .exceptionally { throwable: Throwable? ->
//                    displayError(throwable)
//                    throw CompletionException(throwable)
//                }

    }

    private fun setColor(color: com.google.ar.sceneform.rendering.Color) {
        MaterialFactory.makeOpaqueWithColor(requireContext(), color)
                .thenAccept { material1: com.google.ar.sceneform.rendering.Material -> material = material1.makeCopy() }
//                .exceptionally { throwable: Throwable? ->
//                    displayError(throwable)
//                    throw CompletionException(throwable)
//                }
//        colorPanel.setVisibility(View.GONE)
//        controlPanel.setVisibility(View.VISIBLE)
    }

    override fun onPeekTouch(hitTestResult: HitTestResult?, tap: MotionEvent) {
        val action = tap.action
        val camera: Camera = arFragment.getArSceneView().getScene().getCamera()
        val ray = camera.screenPointToRay(tap.x, tap.y)
        val drawPoint = ray.getPoint(DRAW_DISTANCE)
        if (action == MotionEvent.ACTION_DOWN) {
            if (!::anchorNode.isInitialized || anchorNode == null) {
                val arSceneView: ArSceneView = arFragment.getArSceneView()
                val coreCamera = arSceneView.arFrame!!.camera
                if (coreCamera.trackingState != TrackingState.TRACKING) {
                    return
                }
                val pose = coreCamera.pose
                anchorNode = AnchorNode(arSceneView.session!!.createAnchor(pose))
                anchorNode.setParent(arSceneView.scene)
            }
            currentStroke = Stroke(anchorNode, material)
            strokes.add(currentStroke)
            currentStroke.add(drawPoint)
        } else if (action == MotionEvent.ACTION_MOVE && currentStroke != null) {
            currentStroke.add(drawPoint)
        }
    }


    private fun initData() {
    }


    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvRed -> {
                    setColor(RED)
                }
                binding.tvBack -> {
//                    setColor(BLUE)
                    if (strokes.size < 1) {
                        return
                    }
                    val lastIndex = strokes.size - 1
                    strokes[lastIndex].clear()
                    strokes.removeAt(lastIndex)
                }
                binding.tvBlue -> {
                    setColor(BLUE)
                }
                binding.tvMultiColor -> {
                    setTexture(R.drawable.rainbow_texture)
                }
                binding.tvClear -> {
                    for (stroke in strokes) {
                        stroke.clear()
                    }
                    strokes.clear()
                }

                else -> {

                }
            }
        }
    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"
        private val WHITE = com.google.ar.sceneform.rendering.Color(Color.WHITE)
        private val RED = com.google.ar.sceneform.rendering.Color(Color.RED)
        private val GREEN = com.google.ar.sceneform.rendering.Color(Color.GREEN)
        private val BLUE = com.google.ar.sceneform.rendering.Color(Color.BLUE)
        private val BLACK = com.google.ar.sceneform.rendering.Color(Color.BLACK)
        private const val DRAW_DISTANCE = 0.13f

        fun newInstance(id: Long = 0L): Fragment = DrawLineFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }

    override fun onUpdate(p0: FrameTime?) {
    }


}
