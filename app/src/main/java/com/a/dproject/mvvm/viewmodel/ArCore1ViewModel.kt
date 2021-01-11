package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class ArCore1ViewModel(application: Application) : BaseViewModel<List<ArCore1ViewModel.ArCore1DataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class ArCore1DataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
