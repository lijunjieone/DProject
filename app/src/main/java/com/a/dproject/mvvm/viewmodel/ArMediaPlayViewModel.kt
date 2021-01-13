package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class ArMediaPlayViewModel(application: Application) :
    BaseViewModel<List<ArMediaPlayViewModel.ArMediaPlayDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class ArMediaPlayDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
