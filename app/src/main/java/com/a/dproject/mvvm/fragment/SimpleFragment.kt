package com.a.dproject.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.mvvm.viewmodel.SimpleViewModel
import org.kotlin.annotationProcessor.TestAnnotation

@TestAnnotation
class SimpleFragment : Fragment() {

    companion object {
        fun newInstance() = SimpleFragment()
    }

    private lateinit var viewModel: SimpleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    fun getList(): ArrayList<String> {
        val l = ArrayList<String>()
        l.add("test1")
        l.add("test2")
        return l
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SimpleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
