package com.a.dproject.mvvm.fragment


import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.utils.RotatingNode
import com.a.dproject.ar.utils.SolarSettings
import com.a.dproject.databinding.FragmentHelloArBinding
import com.a.dproject.mvvm.viewmodel.HelloArViewModel
import com.a.dproject.showFragment
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment.OnTapArPlaneListener
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Consumer
import java.util.function.Function


@ListFragmentAnnotation("arCore学习Hello Ar", parentName = "ArCore")
class HelloArFragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentHelloArBinding
    lateinit var viewModel: HelloArViewModel
    private var andyRenderable: ModelRenderable? = null
    private var arFragment: ArFragment? = null
    private var viewRenderable: ViewRenderable? = null
    private var isView: Boolean = false

    private var isAnim: Boolean = false
    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_hello_ar
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
        viewModel = ViewModelProviders.of(this).get(HelloArViewModel::class.java)
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


        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("Sol.sfb"))
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable -> andyRenderable = renderable })
                .exceptionally(
                        Function<Throwable, Void?> { throwable: Throwable? ->
                            val toast: Toast = Toast.makeText(requireContext(), "Unable to load andy renderable", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                            null
                        })
//        ViewRenderable.builder()
//                .setView(requireContext(),R.layout.activity_main2)
//                .build()
//                .thenAccept {
//                    viewRenderable = it
//                }


    }

    private fun initData() {
    }

    val solarSettings = SolarSettings()
    val orbitDegreesPerSecond = 48f
    override fun onResume() {
        super.onResume()
        arFragment = ArFragment()

        R.id.arFragment.showFragment(arFragment!!, childFragmentManager)
        ViewRenderable.builder().setView(requireContext(), R.layout.activity_main2).build().thenAccept {
            viewRenderable = it
        }


        arFragment?.setOnTapArPlaneListener(
                OnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->

                    // Create the Anchor.
                    val anchor = hitResult.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arFragment?.getArSceneView()?.getScene())

                    // Create the transformable andy and add it to the anchor.


                    val andy = getNode()
//                    orbit.setParent(parent)
                    andy.setParent(anchorNode)
                    if (isView) {
                        andy.renderable = viewRenderable
                    } else {
                        andy.renderable = andyRenderable
                    }
//                    andy.select()
                })
    }

    fun getNode(): Node {
        if (isAnim) {
            val andy = RotatingNode(solarSettings, true, false, 0.3f)
            andy.setDegreesPerSecond(orbitDegreesPerSecond)
            return andy
        } else {
            val andy = TransformableNode(arFragment?.getTransformationSystem())
            return andy
//                    val andy = Node()
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvIsView -> {
                    isView = !isView
                    val t = if (isView) "2D" else "3D"
                    "模式为${t}".toast()
                }
                binding.tvViewAnim -> {
                    isAnim = !isAnim
                    val t1 = if (isAnim) "旋转" else "不旋转"
                    "模型${t1}".toast()
                }
                else -> {

                }
            }
        }
    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = HelloArFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
