package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment


class CustomArViewModel(application: Application) : BaseViewModel<List<CustomArViewModel.CustomArDataModel>>(application)  {
    var dataLive = MutableLiveData<Boolean>()
    data class CustomArDataModel(val titleResId: Int, val fragment: Fragment)
    var id = 0L

}
