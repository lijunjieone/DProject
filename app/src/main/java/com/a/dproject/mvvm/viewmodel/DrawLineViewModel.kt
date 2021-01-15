package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class DrawLineViewModel(application: Application) : BaseViewModel<List<DrawLineViewModel.DrawLineDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class DrawLineDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
