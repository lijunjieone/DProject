package com.a.dproject.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewRootImpl
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.a.dproject.R
import com.a.dproject.mvvm.activity.TestActivity
import com.a.dproject.dagger.Car
import com.a.dproject.databinding.FragmentSimpleBinding
import com.a.dproject.mvvm.viewmodel.SimpleViewModel
import com.a.dproject.toast
import com.a.dproject.utils.DResult
import com.a.helper.utils.Utils
import com.a.helper.window.compact.compact.IWindowChangeListener
import com.a.helper.window.compact.compact.WindowRootViewCompat
import com.a.processor.ListFragmentAnnotation
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wanjian.sak.SAK


@ListFragmentAnnotation("简单例子")
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
        initClick()
    }

    fun initClick() {
        binding.tvEvent.setOnClickListener {
            WindowRootViewCompat.get(requireContext().applicationContext)
                .addWindowChangeListener(object :
                    IWindowChangeListener {
                    override fun onAddWindow(
                        viewRootImpl: ViewRootImpl,
                        view: View
                    ) {
//                        "viewRootImpl ${viewRootImpl.toString()}".toast()
                        Utils.getTopFragment(view).toast()
                    }

                    override fun onRemoveWindow(
                        viewRootImpl: ViewRootImpl,
                        view: View
                    ) {
                        "view ${view.toString()}".toast()
                    }
                })
        }

        binding.tvRootView.setOnClickListener {
            val v = Utils.getRootViewImpl(binding.root as ViewGroup)
            v.toString().toast()
        }

        binding.tvDragger1.setOnClickListener {
            val car = Car()
            car.engineA.run()
            car.engineB.run()
        }
        binding.tvGetJson.setOnClickListener {
//            "test".toast()
            val h = handleJson()
            h?.let {
                h.hiddenCard.rank.toast()
            }
        }

        binding.layoutAgain.setOnClickListener {
//            binding.myfTestContainer.requestLayout()
            val i = Intent();
            i.setClass(activity?.baseContext!!,
                TestActivity::class.java)

            startActivity(i)

        }

        binding.myfTestContainer.setOnLongClickListener {
            binding.myfTestContainer.invalidate()
            true
        }
    }


    fun handleJson(): BlackjackHand? {
        val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        val jsonAdapter = moshi.adapter<BlackjackHand>(BlackjackHand::class.java)
        val blackjackHand = jsonAdapter.fromJson(json)
        return blackjackHand
    }

    val json = """
        {
          "hiddenCard": {
            "rank": "6",
            "suit": "SPADES"
          },
          "visibleCards": [
            {
              "rank": "4",
              "suit": "CLUBS"
            },
            {
              "rank": "A",
              "suit": "HEARTS"
            }
          ]
        }
    """.trimIndent()

    data class BlackjackHand(val hiddenCard: Card, val visibleCards: List<Card>)
    data class Card(val rank: String, val suit: Suit)
    enum class Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }
}
