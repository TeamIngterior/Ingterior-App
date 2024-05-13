package com.ing.ingterior

import android.util.Log

object Logging {
    private const val TAG = "ingterior"
    fun logD(TAG: String, msg: String) {
        Log.d(TAG , "$TAG -> $msg")
    }

    fun logV(TAG: String, msg: String){
        Log.v(TAG, "$TAG -> $msg")
    }

    fun logW(TAG: String, msg: String) {
        Log.w(TAG, "$TAG -> $msg")
    }

    fun logE(TAG: String, msg: String, e: Exception?) {
        Log.e(TAG, "$TAG -> $msg", e)
    }

    fun logE(TAG: String, msg: String, e: Throwable?) {
        Log.e(TAG, "$TAG -> $msg", e)
    }
}