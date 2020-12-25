package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class SurfaceViewViewModel(application: Application) : BaseViewModel<List<SurfaceViewViewModel.SurfaceViewDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SurfaceViewDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
