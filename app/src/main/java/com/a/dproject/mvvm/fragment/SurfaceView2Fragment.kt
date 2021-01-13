package com.a.dproject.mvvm.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.utils.DemoUtils
import com.a.dproject.databinding.FragmentSurfaceView2Binding
import com.a.dproject.mvvm.viewmodel.SurfaceView2ViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.Config.LightEstimationMode
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.Scene.OnUpdateListener


@ListFragmentAnnotation("arCore学习", parentName = "ArCore")
class SurfaceView2Fragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentSurfaceView2Binding
    lateinit var viewModel: SurfaceView2ViewModel

    lateinit var arSceneView: ArSceneView
    var id: Long = 0L


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


        // Set a touch listener on the Scene to listen for taps.
        binding.arSceneView
            .getScene()
            .setOnTouchListener(
                Scene.OnTouchListener { hitTestResult: HitTestResult?, event: MotionEvent? ->
                    // If the solar system hasn't been placed yet, detect a tap and then check to see if
                    // the tap occurred on an ARCore plane to place the solar system.
//                    if (!hasPlacedSolarSystem) {
//                        return@setOnTouchListener gestureDetector.onTouchEvent(event)
//                    }
                    event.toString().toast()
                    false
                })

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        binding.arSceneView
            .getScene()
            .addOnUpdateListener(
                OnUpdateListener { frameTime: FrameTime? ->
                    frameTime.toString().toast()
//                    if (loadingMessageSnackbar == null) {
//                        return@addOnUpdateListener
//                    }
//                    val frame: Frame = arSceneView.getArFrame() ?: return@addOnUpdateListener
//                    if (frame.camera.trackingState != TrackingState.TRACKING) {
//                        return@addOnUpdateListener
//                    }
//                    for (plane in frame.getUpdatedTrackables(
//                        Plane::class.java
//                    )) {
//                        if (plane.trackingState == TrackingState.TRACKING) {
//                            hideLoadingMessage()
//                        }
//                    }
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


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = SurfaceView2Fragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
