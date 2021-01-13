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
import com.a.dproject.databinding.FragmentArFaceBinding
import com.a.dproject.mvvm.viewmodel.ArFaceViewModel
import com.a.dproject.showFragment
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.AugmentedFace
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.AugmentedFaceNode
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.*
import java.util.function.Consumer


@ListFragmentAnnotation("脸部识别", parentName = "ArCore")
class ArFaceFragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentArFaceBinding
    lateinit var viewModel: ArFaceViewModel

    private var faceRegionsRenderable: ModelRenderable? = null
    private var faceMeshTexture: Texture? = null

    private var arFragment: FaceArFragment? = null
    private var viewRenderable: ViewRenderable? = null
    private val faceNodeMap = HashMap<AugmentedFace, AugmentedFaceNode>()


    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_ar_face
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
        viewModel = ViewModelProviders.of(this).get(ArFaceViewModel::class.java)
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
            .setSource(requireContext(), Uri.parse("fox_face.sfb"))
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable ->
                faceRegionsRenderable = renderable
                renderable.setShadowCaster(false)
                renderable.setShadowReceiver(false)
            })

    }

    private fun initData() {
    }


    override fun onResume() {
        super.onResume()
        arFragment = FaceArFragment()

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
                andy.renderable = faceRegionsRenderable
            })


        // Load the face mesh texture.
        Texture.builder()
            .setSource(requireContext(), R.drawable.fox_face_mesh_texture)
            .build()
            .thenAccept(Consumer { texture: Texture ->
                faceMeshTexture = texture
            })

        val sceneView = arFragment!!.arSceneView

        // This is important to make sure that the camera stream renders first so that
        // the face mesh occlusion works correctly.

        // This is important to make sure that the camera stream renders first so that
        // the face mesh occlusion works correctly.
        sceneView.cameraStreamRenderPriority = Renderable.RENDER_PRIORITY_FIRST

        val scene = sceneView.scene

        scene.addOnUpdateListener { frameTime: FrameTime? ->
            if (faceRegionsRenderable == null || faceMeshTexture == null) {
                return@addOnUpdateListener
            }
            val faceList =
                sceneView.session!!.getAllTrackables(
                    AugmentedFace::class.java
                )

            // Make new AugmentedFaceNodes for any new faces.
            for (face in faceList) {
                if (!faceNodeMap.containsKey(face)) {
                    val faceNode = AugmentedFaceNode(face)
                    faceNode.setParent(scene)
                    faceNode.faceRegionsRenderable = faceRegionsRenderable
                    faceNode.faceMeshTexture = faceMeshTexture
                    faceNodeMap.put(face, faceNode)
                }
            }

            // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
            val iter: MutableIterator<Map.Entry<AugmentedFace, AugmentedFaceNode>> =
                faceNodeMap.entries.iterator()
            while (iter.hasNext()) {
                val entry =
                    iter.next()
                val face = entry.key
                if (face.trackingState == TrackingState.STOPPED) {
                    val faceNode = entry.value
                    faceNode.setParent(null)
                    iter.remove()
                }
            }
        }
    }


    private fun getNode(): Node {
        val andy = TransformableNode(arFragment?.getTransformationSystem())
        return andy
    }


    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvIsView -> {
                    "message".toast()
                }
                else -> {

                }
            }
        }
    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ArFaceFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
