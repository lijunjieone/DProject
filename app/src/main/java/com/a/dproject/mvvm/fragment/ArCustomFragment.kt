package com.a.dproject.mvvm.fragment



import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.ar.fragment.CustomArCoreFragment
import com.a.dproject.databinding.FragmentCustomArBinding
import com.a.dproject.mvvm.viewmodel.CustomArViewModel
import com.a.dproject.showFragment
import com.a.processor.ListFragmentAnnotation
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.function.Consumer

@ListFragmentAnnotation("arCore检测墙壁", parentName = "ArCore")
class ArCustomFragment : ArtBaseFragment() , View.OnClickListener{

    protected lateinit var binding: FragmentCustomArBinding
    lateinit var viewModel: CustomArViewModel

    var id:Long = 0L
    private lateinit var arFragment: ArFragment
    private lateinit var andyRenderable: ModelRenderable
    private lateinit var viewRenderable: ViewRenderable

    override fun getContentId(): Int {
        return R.layout.fragment_custom_ar
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
        viewModel = ViewModelProviders.of(this).get(CustomArViewModel::class.java)
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
        arFragment = CustomArCoreFragment()
        R.id.arFragment.showFragment(arFragment, childFragmentManager)



        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("fox_face.sfb"))
                .build()
                .thenAccept(Consumer { renderable: ModelRenderable ->
                    andyRenderable = renderable
                })

        ViewRenderable.builder().setView(requireContext(), R.layout.activity_simple).build()
                .thenAccept {
                    viewRenderable = it
                }
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            val andy = TransformableNode(arFragment.transformationSystem)
            andy.renderable = viewRenderable
            andy.setParent(anchorNode)
        }

    }
    private fun initData() {
    }



    override fun onClick(p0: View?) {
        p0?.let {
            when (it) {

                else -> {

                }
            }
        }
    }

        
    companion object {

        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ArCustomFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }



}
