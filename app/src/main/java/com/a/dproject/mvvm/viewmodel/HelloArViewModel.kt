package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class HelloArViewModel(application: Application) :
    BaseViewModel<List<HelloArViewModel.HelloArDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class HelloArDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
