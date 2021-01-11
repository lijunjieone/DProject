package com.a.dproject.mvvm.fragment


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentArCore1Binding
import com.a.dproject.mvvm.viewmodel.ArCore1ViewModel
import com.a.dproject.toast
import com.a.processor.ListFragmentAnnotation
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.permissionx.guolindev.PermissionX
import com.skydoves.pokedexar_core.ModelRenderer
import com.skydoves.pokedexar_core.PokemonModels


@ListFragmentAnnotation("arCore学习1", parentName = "ArCore")
class ArCore1Fragment : ArtBaseFragment(), View.OnClickListener {

    protected lateinit var binding: FragmentArCore1Binding
    lateinit var viewModel: ArCore1ViewModel

    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_ar_core1
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

    private fun initializeModels(arFragment: ArFragment, session: Session) {
        if (session.allAnchors.isEmpty()) {
            val pose = Pose(floatArrayOf(0f, 0f, -1f), floatArrayOf(0f, 0f, 0f, 1f))
            session.createAnchor(pose).apply {
                val garden = PokemonModels.getGarden()
                ModelRenderer.renderObject(requireContext(), garden) { renderable ->
                    ModelRenderer.addGardenOnScene(arFragment, this, renderable, garden)
                }


                val pokemon = PokemonModels.getPokemonByName("bulbasaur")
                        .copy(localPosition = PokemonModels.DEFAULT_POSITION_DETAILS_POKEMON)
                ModelRenderer.renderObject(requireContext(), pokemon) { renderable ->
                    ModelRenderer.addPokemonOnScene(arFragment, this, renderable, pokemon)
                }

                ViewRenderable.builder().setView(requireContext(), R.layout.activity_simple).build().thenAccept {
                    ModelRenderer.addGardenOnScene(arFragment, this, it, garden)
                }

            }
        }
    }


    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().replace(R.id.arFragment, fragment, fragment.javaClass.simpleName).commitNow()
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArCore1ViewModel::class.java)
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

    override fun onResume() {
        super.onResume()
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                    } else {
                        "These permissions are denied: $deniedList".toast()
                    }
                }

        val arFragment = ArFragment()
        showFragment(arFragment)
        with(arFragment) {
            planeDiscoveryController.hide()
//        val text = TextView(requireContext())
//      text.text = "test"
            planeDiscoveryController.setInstructionView(null)
            arSceneView.planeRenderer.isVisible = true
            arSceneView.scene.addOnUpdateListener {
                onUpdate(it)

                // checks the state of the AR frame is Tracking.
                val arFrame = arSceneView.arFrame ?: return@addOnUpdateListener
                if (arFrame.camera?.trackingState != TrackingState.TRACKING) {
                    return@addOnUpdateListener
                }

                // initialize the global anchor with default rendering models.
                arSceneView.session?.let { session ->
                    initializeModels(this, session)
                }
            }
        }

    }

    private fun initData() {
    }


    override fun onClick(p0: View?) {

        p0?.let {
            when (it) {
                binding.tvEvent -> {
                    "event".toast()
                }
                else -> {

                }
            }
        }

    }


    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ArCore1Fragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
