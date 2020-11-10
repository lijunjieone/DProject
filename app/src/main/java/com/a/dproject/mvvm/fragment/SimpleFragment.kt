package com.a.dproject.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.databinding.FragmentSimpleBinding
import com.a.dproject.mvvm.viewmodel.SimpleViewModel
import com.a.dproject.toast
import com.a.dproject.utils.DResult
import com.a.processor.ListFragmentAnnotation
import com.wanjian.sak.SAK

@ListFragmentAnnotation
class SimpleFragment : ArtBaseFragment() {

    companion object {
        fun newInstance() = SimpleFragment()
    }

    protected lateinit var binding: FragmentSimpleBinding

    private lateinit var viewModel: SimpleViewModel

    override fun getContentId(): Int {
        return R.layout.fragment_simple
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getContentId(), container, false)

        return binding.root
    }

    fun testDResult(index: Int): DResult<String> {
//        return DResult.Success("test")
        return when {
            index % 2 == 0 -> {
                DResult.Success("success.${index}")
            }
            index % 3 == 0 -> {
                DResult.Error(Exception("err.${index}"))
            }
            else -> {
                DResult.Loading
            }
        }
    }

    var count = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SimpleViewModel::class.java)
        binding.root.setOnClickListener {
//            "toast".toast()
            testDResult(count++).toString().toast()
        }
        binding.root.setOnLongClickListener {
            SAK.init(requireActivity().application, null)
            true
        }
    }

    fun initClick() {
        binding.tvEvent.setOnClickListener {

        }
    }

}
