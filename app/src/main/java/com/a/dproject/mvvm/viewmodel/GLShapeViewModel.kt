package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment


class GLShapeViewModel(application: Application) : BaseViewModel<List<GLShapeViewModel.GLShapeDataModel>>(application)  {
    var dataLive = MutableLiveData<Boolean>()
    data class GLShapeDataModel(val titleResId: Int, val fragment: Fragment)
    var id = 0L

}
