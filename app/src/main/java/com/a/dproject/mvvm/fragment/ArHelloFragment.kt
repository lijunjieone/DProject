package com.a.dproject.mvvm.fragment


import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.utils.Planet
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
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment.OnTapArPlaneListener
import com.google.ar.sceneform.ux.TransformableNode

import java.util.function.Consumer
import java.util.function.Function


@ListFragmentAnnotation("arCore学习Hello Ar", parentName = "ArCore")
class ArHelloFragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentHelloArBinding
    lateinit var viewModel: HelloArViewModel
    private var andyRenderable: ModelRenderable? = null
    private var duckRenderable: ModelRenderable? = null
    private var earthRenderable: ModelRenderable? = null
    private var lunaRenderable: ModelRenderable? = null
    private var arFragment: ArFragment? = null
    private var viewRenderable: ViewRenderable? = null
    private var isView: Boolean = false
    private var isAnim: Boolean = false
    private var isRotate: Boolean = false
    private var isDuck: Boolean = false
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
        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("Earth.sfb"))
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable -> earthRenderable = renderable })
                .exceptionally(
                        Function<Throwable, Void?> { throwable: Throwable? ->
                            val toast: Toast = Toast.makeText(requireContext(), "Unable to load andy renderable", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                            null
                        })
        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("Luna.sfb"))
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable -> lunaRenderable = renderable })
                .exceptionally(
                        Function<Throwable, Void?> { throwable: Throwable? ->
                            val toast: Toast = Toast.makeText(requireContext(), "Unable to load andy renderable", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                            null
                        })

    }

    private fun initData() {
        val GLTF_ASSET = "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"

        ModelRenderable.builder()
                .setSource(requireContext(), RenderableSource.builder().setSource(
                        requireContext(),
                        Uri.parse(GLTF_ASSET),
                        RenderableSource.SourceType.GLTF2)
                        .setScale(0.5f) // Scale the original model to 50%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(GLTF_ASSET)
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable -> duckRenderable = renderable })

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
                    if (isDuck) {
                        andy.renderable = duckRenderable
                    } else if (isView) {
                        andy.renderable = viewRenderable
                    } else {
                        andy.renderable = andyRenderable
                    }
//                    andy.select()
                })
    }

    private fun getNode(): Node {
        if (isAnim) {
            val andy = RotatingNode(solarSettings, true, false, 0.3f)
            andy.setDegreesPerSecond(orbitDegreesPerSecond)
            return andy
        } else if (isRotate) {
            val andy = TransformableNode(arFragment?.getTransformationSystem())
            val earth = createPlanet("Earth", andy, 1.0f, 29f, earthRenderable!!, 0.05f, 23.4f)
            createPlanet("Moon", earth!!, 0.15f, 100f, lunaRenderable!!, 0.018f, 6.68f)
            return andy
        } else {
            val andy = TransformableNode(arFragment?.getTransformationSystem())
            return andy
        }
    }


    private fun createPlanet(
            name: String,
            parent: Node,
            auFromParent: Float,
            orbitDegreesPerSecond: Float,
            renderable: ModelRenderable,
            planetScale: Float,
            axisTilt: Float): Node? {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.
        val orbit = RotatingNode(solarSettings, true, false, 0.0f)
        orbit.setDegreesPerSecond(orbitDegreesPerSecond)
        orbit.setParent(parent)

        // Create the planet and position it relative to the sun.
        val planet = Planet(
                requireContext(), name, planetScale, orbitDegreesPerSecond, axisTilt, renderable, solarSettings)
        planet.setParent(orbit)
        planet.setLocalPosition(Vector3(auFromParent * AU_TO_METERS, 0.0f, 0.0f))
        return planet
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvIsView -> {
                    isView = !isView
                    isDuck = false

                    val t = if (isView) "2D" else "3D"
                    "模式为${t}".toast()
                }
                binding.tvViewAnim -> {
                    isAnim = !isAnim
                    val t1 = if (isAnim) "旋转" else "不旋转"
                    "模型${t1}".toast()
                }
                binding.tvViewRotate -> {
                    isRotate = !isRotate
                    isAnim = false
                    val t1 = if (isRotate) "增加公转模型" else "去掉公转模型"
                    t1.toast()
                }
                binding.tvViewDuck -> {
                    isDuck = !isDuck
                    val t1 = if (isDuck) "使用网络下载的小黄鸭模型" else "使用本地模型"
                    t1.toast()
                }
                else -> {

                }
            }
        }
    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"
        private const val AU_TO_METERS = 0.5f

        fun newInstance(id: Long = 0L): Fragment = ArHelloFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
