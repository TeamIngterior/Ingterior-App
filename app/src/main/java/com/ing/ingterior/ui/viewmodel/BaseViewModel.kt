package com.ing.ingterior.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ing.ingterior.util.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun isNetworkConnected(context: Context): Boolean {
        return NetworkState(context).isNetworkConnected()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun showLog(msg: Any){
        Log.d("확인", "$msg")
    }
}