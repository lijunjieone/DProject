package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ListViewModel(application: Application) :
    BaseViewModel<List<ListViewModel.ListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

    data class ListDataModel(val letter: String, val position: Int)


    override fun loadData(): LiveData<List<ListViewModel.ListDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ListViewModel.ListDataModel>()
        val size = itemList.value?.size ?: 1

        if (canReload) {
            for (i in 0..50) {
                val model = ListViewModel.ListDataModel("Item: ${i}", i)
                list.add(model)
            }
        } else if (canLoadMore) {
            for (i in size..size + 50) {
                val model = ListViewModel.ListDataModel("Item: ${i}", i)
                list.add(model)
            }
        }
        canLoadMore = size <= 150
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}