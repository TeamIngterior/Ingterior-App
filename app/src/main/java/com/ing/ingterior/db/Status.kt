package com.ing.ingterior.db

import java.lang.Thread.State

sealed class Status<out T> {
    data class Success<out T>(val data: T) : Status<T>()
    data class Error(val message: String) : Status<Nothing>()
    object Loading : Status<Nothing>()
}
