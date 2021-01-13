package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class ArFaceViewModel(application: Application) :
    BaseViewModel<List<ArFaceViewModel.ArFaceDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class ArFaceDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
