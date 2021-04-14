package com.a.dproject.mvvm.fragment



import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.a.dproject.R
import com.a.dproject.databinding.FragmentSnapshotListBinding
import com.a.dproject.mvvm.viewmodel.SnapshotListViewModel
import com.a.dproject.views.adapter.SnapshotListAdapter
import android.os.Handler
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.fragment.app.Fragment
import com.a.dproject.views.CommonMultiItem
import com.a.processor.ListFragmentAnnotation

@ListFragmentAnnotation("本地长图","snapshot")
class SnapshotListFragment : ArtBaseFragment() {

    protected lateinit var binding: FragmentSnapshotListBinding
    lateinit var viewModel: SnapshotListViewModel
    private lateinit var adapter: SnapshotListAdapter
    var id: Long = 0L


    override fun getContentId(): Int {
        return R.layout.fragment_snapshot_list
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
        viewModel = ViewModelProviders.of(this).get(SnapshotListViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id
    }


    private fun initObserver() {
        viewModel.itemList.observe(this, Observer {
            it?.let {
                val items = it.map {
                    CommonMultiItem<SnapshotListViewModel.SnapshotListDataModel>(
                        if(it.letter.startsWith("Item: 1") ) CommonMultiItem.ITEM_HEADER else CommonMultiItem.ITEM_ONE,
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
        adapter =  SnapshotListAdapter(ArrayList<CommonMultiItem<SnapshotListViewModel.SnapshotListDataModel>>())
        binding.recyclerView.adapter = adapter
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

        fun newInstance(id: Long = 0L): Fragment = SnapshotListFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}

