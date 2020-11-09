package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import test.generated.getShowList

class ListViewModel(application: Application) :
    BaseViewModel<List<ListViewModel.ListDataModel>>(application) {

    var dataLive = MutableLiveData<Boolean>()
    var loading = false
    var id = 0L

    init {
        canReload = true
        canLoadMore = false
    }

    data class ListDataModel(val letter: String, val fragmentName: String)


    override fun loadData(): LiveData<List<ListViewModel.ListDataModel>> {
        if (loading) {
            return itemList
        }
        val list = ArrayList<ListViewModel.ListDataModel>()
        getShowList().forEach {
            list.add(ListDataModel(it, it))
        }
//        list.add(ListDataModel("SimpleFragment", "SimpleFragment"))
//        list.add(ListDataModel("TabFragment", "TabFragment"))
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}