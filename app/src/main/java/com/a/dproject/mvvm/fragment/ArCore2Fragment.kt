package com.a.dproject.mvvm.fragment


import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.utils.DemoUtils
import com.a.dproject.ar.utils.Planet
import com.a.dproject.ar.utils.RotatingNode
import com.a.dproject.ar.utils.SolarSettings
import com.a.dproject.databinding.FragmentSurfaceView2Binding
import com.a.dproject.mvvm.viewmodel.SurfaceView2ViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.Config.LightEstimationMode
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.*
import com.google.ar.sceneform.Scene.OnUpdateListener
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable


@ListFragmentAnnotation("arCore学习ArSceneView", parentName = "ArCore")
class ArCore2Fragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentSurfaceView2Binding
    lateinit var viewModel: SurfaceView2ViewModel

    lateinit var arSceneView: ArSceneView
    var id: Long = 0L

    private val solarSettings: SolarSettings = SolarSettings()

    override fun getContentId(): Int {
        return R.layout.fragment_surface_view2
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
        viewModel = ViewModelProviders.of(this).get(SurfaceView2ViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id

    }


    private fun initObserver() {
    }

    override fun onPause() {
        super.onPause()
        if (arSceneView != null) {
            arSceneView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (arSceneView != null) {
            arSceneView.destroy()
        }
    }

    override fun onResume() {
        super.onResume()
        if (arSceneView == null) {
            return
        }

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                val lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR
                val session: Session =
                    DemoUtils.createArSessionWithInstallRequest(
                            requireActivity(),
                            lightEstimationMode
                    )
                arSceneView.setupSession(session)
            } catch (e: UnavailableException) {
                DemoUtils.handleSessionException(requireActivity(), e)
            }
        }

        try {
            arSceneView.resume()
        } catch (ex: CameraNotAvailableException) {
            DemoUtils.displayError(requireContext(), "Unable to get camera", ex)
            return
        }

//        if (arSceneView.getSession() != null) {
//            showLoadingMessage()
//        }
    }

    private fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.onClickListener = this
        arSceneView = binding.arSceneView


        // Set up a tap gesture detector.
        val gestureDetector = GestureDetector(
                requireContext(),
                object : SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        onSingleTap(e)
//                        e.toString().toast()
                        return true
                    }

                    override fun onDown(e: MotionEvent): Boolean {
                        return true
                    }
                })

        binding.arSceneView.scene.setOnTouchListener { hitTestResult, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
        }


        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        binding.arSceneView
                .getScene()
                .addOnUpdateListener(
                        OnUpdateListener { frameTime: FrameTime? ->

                            val frame: Frame? = arSceneView.getArFrame()
                            frame?.let {
                                if (frame.camera.trackingState == TrackingState.TRACKING) {
                                    for (plane in frame.getUpdatedTrackables(
                                            Plane::class.java
                                    )) {
                                        if (plane.trackingState == TrackingState.TRACKING) {
//                            hideLoadingMessage()
                                        }
                                    }
                                }
                            }
                        })



    }

    private fun initData() {
    }


    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {
                binding.tvEvent -> {
                    "event".toast()
                }
            }
        }
    }


    private fun onSingleTap(tap: MotionEvent) {

        val frame = arSceneView.arFrame
        if (frame != null) {
            tryPlaceSolarSystem(tap, frame)
        }
    }

    private fun tryPlaceSolarSystem(tap: MotionEvent?, frame: Frame): Boolean {
        if (tap != null && frame.camera.trackingState == TrackingState.TRACKING) {
            for (hit in frame.hitTest(tap)) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    // Create the Anchor.
                    val anchor = hit.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arSceneView.scene)
                    val solarSystem = createSolarSystem()
                    anchorNode.addChild(solarSystem)
                    return true
                }
            }
        }
        return false
    }

    private fun createSolarSystem(): Node {
        val base = Node()
        val sun = Node()
        sun.setParent(base)
        sun.localPosition = Vector3(0.0f, 0.5f, 0.0f)
        val sunVisual = Node()
        sunVisual.setParent(sun)
        val sunRenderable = ModelRenderable.builder().setSource(requireContext(), Uri.parse("Sol.sfb")).build()
        sunVisual.renderable = sunRenderable.get()
        sunVisual.localScale = Vector3(0.5f, 0.5f, 0.5f)
        val solarControls = Node()
//        solarControls.setParent(sun)
//        solarControls.renderable = solarControlsRenderable
//        solarControls.localPosition = Vector3(0.0f, 0.25f, 0.0f)
//        val solarControlsView: View = solarControlsRenderable.getView()
//        val orbitSpeedBar = solarControlsView.findViewById<SeekBar>(R.id.orbitSpeedBar)
//        orbitSpeedBar.progress = (solarSettings.getOrbitSpeedMultiplier() * 10.0f)
//        orbitSpeedBar.setOnSeekBarChangeListener(
//                object : OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                        val ratio = progress.toFloat() / orbitSpeedBar.max.toFloat()
//                        solarSettings.setOrbitSpeedMultiplier(ratio * 10.0f)
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
//                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
//                })
//        val rotationSpeedBar = solarControlsView.findViewById<SeekBar>(R.id.rotationSpeedBar)
//        rotationSpeedBar.progress = (solarSettings.getRotationSpeedMultiplier() * 10.0f)
//        rotationSpeedBar.setOnSeekBarChangeListener(
//                object : OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                        val ratio = progress.toFloat() / rotationSpeedBar.max.toFloat()
//                        solarSettings.setRotationSpeedMultiplier(ratio * 10.0f)
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
//                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
//                })

        // Toggle the solar controls on and off by tapping the sun.
        sunVisual.setOnTapListener { hitTestResult: HitTestResult?, motionEvent: MotionEvent? -> solarControls.isEnabled = !solarControls.isEnabled }
        val mercuryRenderable = ModelRenderable.builder().setSource(requireContext(), Uri.parse("Mercury.sfb")).build()
        createPlanet("Mercury", sun, 0.4f, 47f, mercuryRenderable.get(), 0.019f, 0.03f)
//        createPlanet("Venus", sun, 0.7f, 35f, venusRenderable, 0.0475f, 2.64f)
//        val earth = createPlanet("Earth", sun, 1.0f, 29f, earthRenderable, 0.05f, 23.4f)
//        createPlanet("Moon", earth, 0.15f, 100f, lunaRenderable, 0.018f, 6.68f)
//        createPlanet("Mars", sun, 1.5f, 24f, marsRenderable, 0.0265f, 25.19f)
//        createPlanet("Jupiter", sun, 2.2f, 13f, jupiterRenderable, 0.16f, 3.13f)
//        createPlanet("Saturn", sun, 3.5f, 9f, saturnRenderable, 0.1325f, 26.73f)
//        createPlanet("Uranus", sun, 5.2f, 7f, uranusRenderable, 0.1f, 82.23f)
//        createPlanet("Neptune", sun, 6.1f, 5f, neptuneRenderable, 0.074f, 28.32f)
        return base
    }

    private fun createPlanet(
            name: String,
            parent: Node,
            auFromParent: Float,
            orbitDegreesPerSecond: Float,
            renderable: ModelRenderable,
            planetScale: Float,
            axisTilt: Float): Node {
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


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"
        private const val AU_TO_METERS = 0.5f

        fun newInstance(id: Long = 0L): Fragment = ArCore2Fragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
