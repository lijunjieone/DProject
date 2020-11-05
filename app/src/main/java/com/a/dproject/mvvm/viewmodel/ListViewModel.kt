package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.dproject.mvvm.fragment.SimpleFragment

class ListViewModel(application: Application) :
    BaseViewModel<List<ListViewModel.ListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

    data class ListDataModel(val letter: String, val fragment: Fragment)


    override fun loadData(): LiveData<List<ListViewModel.ListDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ListViewModel.ListDataModel>()
        val simpleFragment = SimpleFragment.newInstance()
        list.add(ListDataModel("SimpleFragment", simpleFragment))
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}