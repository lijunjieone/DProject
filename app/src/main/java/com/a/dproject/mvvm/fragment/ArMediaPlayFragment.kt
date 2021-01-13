package com.a.dproject.mvvm.fragment


import android.graphics.SurfaceTexture
import android.media.MediaPlayer
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
import com.a.dproject.databinding.FragmentArMediaPlayBinding
import com.a.dproject.mvvm.viewmodel.ArMediaPlayViewModel
import com.a.dproject.showFragment
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ExternalTexture
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Consumer


@ListFragmentAnnotation("arCore播放视频", parentName = "ArCore")
class ArMediaPlayFragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentArMediaPlayBinding
    lateinit var viewModel: ArMediaPlayViewModel

    var id: Long = 0L
    private lateinit var andyRenderable: ModelRenderable
    private var arFragment: ArFragment? = null
    private lateinit var mediaPlayer: MediaPlayer

    private val CHROMA_KEY_COLOR = Color(0.1843f, 1.0f, 0.098f)

    override fun getContentId(): Int {
        return R.layout.fragment_ar_media_play
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
        viewModel = ViewModelProviders.of(this).get(ArMediaPlayViewModel::class.java)
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

    override fun onResume() {
        super.onResume()
        arFragment = ArFragment()
        R.id.arFragment.showFragment(arFragment!!, childFragmentManager)

        // Create an ExternalTexture for displaying the contents of the video.

        // Create an ExternalTexture for displaying the contents of the video.
        val texture = ExternalTexture()

        // Create an Android MediaPlayer to capture the video on the external texture's surface.

        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.lion_chroma)
        mediaPlayer.setSurface(texture.surface)
        mediaPlayer.isLooping = true
        ModelRenderable.builder()
            .setSource(requireContext(), Uri.parse("chroma_key_video.sfb"))
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable ->
                andyRenderable = renderable
                andyRenderable.getMaterial().setExternalTexture("videoTexture", texture);
                andyRenderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);
            })

        arFragment?.setOnTapArPlaneListener(
            BaseArFragment.OnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->

                // Create the Anchor.
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment?.getArSceneView()?.getScene())

                // Create the transformable andy and add it to the anchor.


//                val videoNode = getNode()
                val videoNode = Node()
                videoNode.setParent(anchorNode)
//                videoNode.renderable = andyRenderable


                // Set the scale of the node so that the aspect ratio of the video is correct.

                // Set the scale of the node so that the aspect ratio of the video is correct.
                val videoWidth = mediaPlayer.videoWidth.toFloat()
                val videoHeight = mediaPlayer.videoHeight.toFloat()
                videoNode.setLocalScale(
                    Vector3(
                        VIDEO_HEIGHT_METERS * (videoWidth / videoHeight),
                        VIDEO_HEIGHT_METERS,
                        1.0f
                    )
                )

                // Start playing the video when the first node is placed.

                // Start playing the video when the first node is placed.
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()

                    // Wait to set the renderable until the first frame of the  video becomes available.
                    // This prevents the renderable from briefly appearing as a black quad before the video
                    // plays.
                    texture
                        .surfaceTexture
                        .setOnFrameAvailableListener { surfaceTexture: SurfaceTexture? ->
                            videoNode.setRenderable(andyRenderable)
                            texture.surfaceTexture.setOnFrameAvailableListener(null)
                        }
                } else {
                    videoNode.setRenderable(andyRenderable)
                }
//                    andy.select()
            })
    }

    private fun getNode(): Node {
        val andy = TransformableNode(arFragment?.getTransformationSystem())
        return andy
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer.release()
        }
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
        private const val VIDEO_HEIGHT_METERS = 0.85f

        fun newInstance(id: Long = 0L): Fragment = ArMediaPlayFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
