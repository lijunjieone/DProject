package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


class TranslateArViewModel(application: Application) :
    BaseViewModel<List<TranslateArViewModel.TranslateArDataModel>>(application) {
    var dataLive = MutableLiveData<Boolean>()

    data class TranslateArDataModel(val titleResId: Int, val fragment: Fragment)

    var id = 0L

}
