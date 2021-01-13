package com.a.dproject.mvvm.fragment


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentTranslateArBinding
import com.a.dproject.mvvm.viewmodel.TranslateArViewModel
import com.a.dproject.showFragment
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.AnimationData
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Consumer


@ListFragmentAnnotation("arCore学习可以运动的Ar", parentName = "ArCore")
class ArAnimFragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentTranslateArBinding
    lateinit var viewModel: TranslateArViewModel
    private var andyRenderable: ModelRenderable? = null
    private var arFragment: ArFragment? = null
    private var viewRenderable: ViewRenderable? = null
    private var animator: ModelAnimator? = null
    private var nextAnimation = 0

    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_translate_ar
    }

    override fun getStoneId(): Int {
        return 0
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel = ViewModelProviders.of(this).get(TranslateArViewModel::class.java)
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

        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse("andy_dance.sfb"))
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable -> andyRenderable = renderable })

    }

    private fun initData() {
    }

    override fun onResume() {
        super.onResume()
        arFragment = ArFragment()

        R.id.arFragment.showFragment(arFragment!!, childFragmentManager)
        ViewRenderable.builder().setView(requireContext(), R.layout.activity_main2).build()
            .thenAccept {
                viewRenderable = it
            }


        arFragment?.setOnTapArPlaneListener(
            BaseArFragment.OnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->

                // Create the Anchor.
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment?.getArSceneView()?.getScene())
                val andy = getNode()
                andy.setParent(anchorNode)
                andy.renderable = andyRenderable
            })
    }

    private fun getNode(): Node {
        val andy = TransformableNode(arFragment?.getTransformationSystem())
        return andy
    }


    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvIsView -> {
                    onPlayAnimation()
                }
                else -> {

                }
            }
        }
    }

    private fun onPlayAnimation() {
        if (animator == null || !animator!!.isRunning()) {
            val data: AnimationData? = andyRenderable!!.getAnimationData(nextAnimation)
            nextAnimation = (nextAnimation + 1) % andyRenderable!!.animationDataCount
            animator = ModelAnimator(data, andyRenderable)
            animator!!.start()
            data?.name?.toast()
        }
    }

    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ArAnimFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
