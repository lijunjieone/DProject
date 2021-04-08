package com.a.dproject.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.fragment.app.Fragment


class FFMpegMediaPlayerViewModel(application: Application) : BaseViewModel<List<FFMpegMediaPlayerViewModel.FFMpegMediaPlayerDataModel>>(application)  {
    var dataLive = MutableLiveData<Boolean>()
    data class FFMpegMediaPlayerDataModel(val titleResId: Int, val fragment: Fragment)
    var id = 0L

}
