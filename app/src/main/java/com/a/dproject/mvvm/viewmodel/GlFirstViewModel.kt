package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment


class GlFirstViewModel(application: Application) : BaseViewModel<List<GlFirstViewModel.GlFirstDataModel>>(application)  {
    var dataLive = MutableLiveData<Boolean>()
    data class GlFirstDataModel(val titleResId: Int, val fragment: Fragment)
    var id = 0L

}
