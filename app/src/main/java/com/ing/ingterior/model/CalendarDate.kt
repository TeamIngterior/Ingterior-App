package com.ing.ingterior.model

import android.util.Log
import com.ing.ingterior.Logging.logD
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

data class CalendarDate(val day: Int,val dayOfWeek: Int, val week: Int, val month: Int, val year: Int) {

    companion object{
        private const val TAG = "CalendarDate"
        fun getCurrentCalendarDate(): CalendarDate {
            val calendar = Calendar.getInstance() // 현재 날짜 캘린더
            val year = calendar.get(Calendar.YEAR) // 현재 연도
            val month = calendar.get(Calendar.MONTH) // 현재 월
            val day = calendar[Calendar.DAY_OF_MONTH] // 현재 일
            val week = calendar[Calendar.WEEK_OF_MONTH]
            val dayOfWeek = calendar[Calendar.DAY_OF_WEEK] // 요일 (1: 일요일, 2: 월요일, ..., 7: 토요일)
            return CalendarDate(day, dayOfWeek, week, (month + 1) % 12, year)
        }
    }

    fun isCompare(dateModel: CalendarDate?) : Int {
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

    fun isSameYearAndMonth(dateModel: CalendarDate?) : Boolean {
        return if(dateModel == null) false
        else {
            year == dateModel.year && month == dateModel.month
        }
    }

    fun isToday(dateModel: CalendarDate?): Boolean{
        return if(dateModel == null) false
        else {
            year == dateModel.year && month == dateModel.month && day == dateModel.day
        }
    }

    fun isSameWeek(dateModel: CalendarDate?): Boolean{
        return if(dateModel == null) false
        else {
            year == dateModel.year && month == dateModel.month && week == dateModel.week
        }
    }

    private fun getWeekString(week: Int):String {
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

    fun toMillis(): Long {
        logD(TAG, "toMillis: ${timeFormat()}")
        val localDate = LocalDate.parse(timeFormat(), DateTimeFormatter.ISO_LOCAL_DATE)
        val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())
        return zonedDateTime.toInstant().toEpochMilli()
    }

    override fun toString(): String {
        return "${year}년 ${month}월 ${week}주 ${day}일 ${getWeekString(dayOfWeek)}요일"
    }

    fun timeFormat(): String{
        logD(TAG, "timeFormat: ${toString()}")
        return "${year}-${if(month<10) "0$month" else month}-${if(day<10) "0$day" else day}"
    }

    fun timeFormat2(): String{
        logD(TAG, "timeFormat: ${toString()}")
        return "${if(month<10) "0$month" else month}-${if(day<10) "0$day" else day}"
    }
}
