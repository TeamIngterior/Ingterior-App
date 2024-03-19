package com.ing.ingterior.model

data class DateModel(val day: Int, val week: Int, val month: Int, val year: Int, var isChecked: Boolean = false) {
    fun isCompare(dateModel: DateModel?) : Int {
        if(dateModel==null) return 1
        if(year > dateModel.year) {
            return 1
        }
        else if(year == dateModel.year && month > dateModel.month) {
            return 1
        }
        else if(year == dateModel.year && month == dateModel.month && day > dateModel.day) {
            return 1
        }
        else if(year == dateModel.year && month == dateModel.month && day == dateModel.day) {
            return 0
        }
        else if(year < dateModel.year) {
            return -1
        }
        else if(month < dateModel.month) {
            return -1
        }
        else {
            return -1
        }
    }

    fun getWeekString(week: Int):String {
        return when(week) {
            0 -> "일"
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            else -> "알 수 없음"
        }
    }

    override fun toString(): String {
        return "${year}년 ${month+1}월 ${day}일 ${getWeekString(week)}요일"
    }
}
