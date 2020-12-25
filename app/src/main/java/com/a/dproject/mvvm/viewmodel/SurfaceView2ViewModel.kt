package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class SurfaceView2ViewModel(application: Application) : BaseViewModel<List<SurfaceView2ViewModel.SurfaceView2DataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class SurfaceView2DataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
