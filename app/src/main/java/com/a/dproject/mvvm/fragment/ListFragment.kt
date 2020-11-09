package com.a.dproject.mvvm.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.a.dproject.R
import com.a.dproject.databinding.FragmentListBinding
import com.a.dproject.mvvm.viewmodel.ListViewModel
import com.a.dproject.views.CommonMultiItem
import com.a.dproject.views.adapter.ListAdapter
import com.a.processor.ListFragmentAnnotation

@ListFragmentAnnotation
class ListFragment : ArtBaseFragment() {

    protected lateinit var binding: FragmentListBinding
    lateinit var viewModel: ListViewModel
    private lateinit var adapter: ListAdapter
    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_list
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
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id
    }


    private fun initObserver() {
        viewModel.itemList.observe(this, Observer {
            it?.let {
                val items = it.map {
                    CommonMultiItem<ListViewModel.ListDataModel>(
                        if (it.letter.startsWith("Item: 1")) CommonMultiItem.ITEM_HEADER else CommonMultiItem.ITEM_ONE,
                        3,
                        it
                    )
                }
                adapter.replaceData(items)

            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

    }

    private fun initView() {
        adapter = ListAdapter(ArrayList<CommonMultiItem<ListViewModel.ListDataModel>>())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //解决刷新闪烁问题
        val animator = binding.recyclerView.itemAnimator as SimpleItemAnimator
        animator.supportsChangeAnimations = false


    }

    private fun initData() {
        viewModel.loadData()
    }


    companion object {
        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = ListFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}

