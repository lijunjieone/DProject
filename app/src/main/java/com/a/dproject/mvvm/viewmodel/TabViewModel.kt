package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class TabViewModel(application: Application) :
    BaseViewModel<List<TabViewModel.TabDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class TabDataModel(val title: String, val fragment: Fragment)

    var id = 0L

}
