package com.a.dproject.mvvm.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentTabBinding
import com.a.dproject.mvvm.viewmodel.TabViewModel


class TabFragment : ArtBaseFragment() {

    protected lateinit var binding: FragmentTabBinding
    lateinit var viewModel: TabViewModel
    private lateinit var adapter: TabAdapter
    var id: Long = 0L


    private val fragments = ArrayList<TabViewModel.TabDataModel>()

    override fun getContentId(): Int {
        return R.layout.fragment_tab
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
        viewModel = ViewModelProviders.of(this).get(TabViewModel::class.java)
        //可以在这里完成外部数据跟viewmodel的通讯
        id = arguments?.getLong(PARAM_DEFAULT_ID) ?: 0L
        viewModel.id = id

    }


    private fun initObserver() {
    }


    private fun initView() {
        initFragments()
        binding.viewPager.adapter = TabAdapter(childFragmentManager)
        binding.viewPager.offscreenPageLimit = 3
        binding.tabLayout.setViewPager(binding.viewPager)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initData() {
    }

    private fun initFragments() {
        fragments.add(TabViewModel.TabDataModel("TAB1", EmptyFragment()))
        fragments.add(TabViewModel.TabDataModel("TAB2", EmptyFragment()))
        fragments.add(TabViewModel.TabDataModel("TAB3", EmptyFragment()))
    }

    inner class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(p0: Int): Fragment {
            return fragments[p0].fragment
        }

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int): CharSequence? {
            return fragments[position].title
        }
    }


    companion object {
        const val PARAM_DEFAULT_ID = "param_default_id"

        fun newInstance(id: Long = 0L): Fragment = TabFragment().apply {
            //在这里完成Fragment和外部数据的通讯
            val args = Bundle()
            args.putLong(PARAM_DEFAULT_ID, id)
            arguments = args
        }
    }


}
