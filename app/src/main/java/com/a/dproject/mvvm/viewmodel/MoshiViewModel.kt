package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class MoshiViewModel(application: Application) :
    BaseViewModel<List<MoshiViewModel.MoshiDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class MoshiDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
