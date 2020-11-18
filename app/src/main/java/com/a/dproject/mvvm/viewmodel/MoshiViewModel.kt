package com.a.dproject.mvvm.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.dproject.di.NetworkModule
import com.a.dproject.mvvm.fragment.MoshiFragment
import com.a.dproject.network.PokedexService
import com.a.dproject.toast
import com.skydoves.pokedex.model.Pokemon
import com.skydoves.pokedex.model.PokemonResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MoshiViewModel(application: Application) :
    BaseViewModel<List<MoshiViewModel.MoshiDataModel>>(application), CoroutineScope {
    var dataLive = MutableLiveData<Boolean>()
    private lateinit var mApiService: PokedexService

    data class MoshiDataModel(val poke: Pokemon)

    var id = 0L

    fun init() {
        val retrofit = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
        mApiService = retrofit.create(PokedexService::class.java)
    }

    fun getDataByRetrofit() {
        val netData: Call<PokemonResponse> = mApiService.fetchPokemonList(0)
        netData.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ) {
                if (response.isSuccessful) {
                    try {
                        response.body()?.next.toString().toast()
                        val list = response.body()?.results
                        list?.let {
                            itemList.value = list.map { MoshiDataModel(it) }
                        }
//                        response.body()!!.string().toast()
//                        Log.i(TAG, response.body()!!.string() + "----" + response.code())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val _result: MutableLiveData<MoshiFragment.Companion.FileDownloadResult> =
        MutableLiveData()
    val result: LiveData<MoshiFragment.Companion.FileDownloadResult> = _result


    fun fileDownload(url: String) {
        launch {
            withContext(Dispatchers.IO) {
                _result.postValue(MoshiFragment.storageFile(url, getApplication()))
            }
        }
    }

}
