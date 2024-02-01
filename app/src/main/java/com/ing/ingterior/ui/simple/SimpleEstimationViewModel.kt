package com.ing.ingterior.ui.simple

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimpleEstimationViewModel : ViewModel() {

    val meterOrPyeong = MutableLiveData(-1)
    var changeSash = -1
    var expandBalcony = -1
    var bathRoomCount = -1

    fun isEnableResult() = changeSash > 0 && expandBalcony >= 0 && bathRoomCount > 0 && (meterOrPyeong.value?.toInt() ?: 0) > 0

}