package com.a.dproject.mvvm.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.dproject.getAnnotationMap

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
        getAnnotationMap().forEach {
            val key = if (TextUtils.isEmpty(it.key)) {
                it.value
            } else {
                it.key
            }
            list.add(ListDataModel(key, it.value))
        }
//        list.add(ListDataModel("SimpleFragment", "SimpleFragment"))
//        list.add(ListDataModel("TabFragment", "TabFragment"))
        //itemList.value = handleListRequestResponse(list=list,haveMore = canLoadMore)
        itemList.value = list
        return itemList
    }


}