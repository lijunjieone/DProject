package com.a.dproject.mvvm.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.a.dproject.di.NetworkModule
import com.a.dproject.network.PokedexService
import com.a.dproject.toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class MoshiViewModel(application: Application) :
    BaseViewModel<List<MoshiViewModel.MoshiDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()
    private lateinit var mApiService: PokedexService

    data class MoshiDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

    fun init() {
        val retrofit = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
        mApiService = retrofit.create(PokedexService::class.java)
    }

    fun getDataByretrofit() {
        val netData: Call<ResponseBody> = mApiService.fetchPokemonList(0)
        netData.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        response.body()!!.string().toast()
                        Log.i(TAG, response.body()!!.string() + "----" + response.code())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

}
