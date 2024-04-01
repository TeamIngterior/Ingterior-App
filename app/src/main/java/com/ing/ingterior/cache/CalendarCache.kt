package com.ing.ingterior.cache

import android.content.Context
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.model.CalendarDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.HashMap

class CalendarCache {
    companion object {
        private const val TAG = "ContactsCache"
        private var instance: CalendarCache? = null
        fun getInstance(): CalendarCache {
            return instance ?: synchronized(this){
                instance ?: CalendarCache().also {
                    instance = it
                }
            }
        }
    }


    private val cacheCalendarList = HashMap<String, ArrayList<CalendarDate>>()

    init {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -7)
        for(i in 0 until 13){
            calendar.add(Calendar.MONTH, 1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val hashKey = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}"
            val hashValues = arrayListOf<CalendarDate>()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val weekDayOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
            // 이전 달의 날짜들로 첫 주 채우기
            calendar.add(Calendar.MONTH, -1) // 이전 달로 설정
            val maxDayOfPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val startDayOfPrevMonth = maxDayOfPrevMonth - weekDayOfFirstDay + 1
            for (day in startDayOfPrevMonth..maxDayOfPrevMonth) {
                val calendarDate = CalendarDate(day, hashValues.size % 7, calendar.getActualMaximum(Calendar.WEEK_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
                hashValues.add(calendarDate)
            }
            calendar.add(Calendar.MONTH, 1) // 다시 현재 달로 설정
            // 현재 달의 날짜들 추가
            val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (day in 1..maxDayOfMonth) {
                val calendarDate = CalendarDate(day, calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_MONTH), month + 1, year)
                hashValues.add(calendarDate)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            // 다시 이전 달의 마지막 날로 이동
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            // 마지막 날짜의 요일 확인
            calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth)
            val lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // 마지막 날짜가 토요일이 아니라면 다음 달의 날짜들로 채움
            if (lastDayOfWeek != Calendar.SATURDAY) {
                val nextMonthDays = 7 - lastDayOfWeek // 토요일까지 필요한 날짜 수
                for (day in 1..nextMonthDays) {
                    val calendarDate = CalendarDate(day, calendar.get(Calendar.DAY_OF_WEEK), 1, (month + 2) % 12, if(month == 11) year + 1 else year)
                    hashValues.add(calendarDate)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                calendar.add(Calendar.MONTH, -1)
            }
            cacheCalendarList[hashKey] = hashValues
        }
    }

    fun getCalendarDate(key: String): ArrayList<CalendarDate>? {
        return cacheCalendarList[key]
    }

    fun getCalendarDate(calendar: Calendar) : ArrayList<CalendarDate> {
        val hashKey = "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)}"
        if(cacheCalendarList.contains(hashKey)) return cacheCalendarList[hashKey]!!
        else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val hashValues = arrayListOf<CalendarDate>()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val weekDayOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
            // 이전 달의 날짜들로 첫 주 채우기
            calendar.add(Calendar.MONTH, -1) // 이전 달로 설정
            val maxDayOfPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val startDayOfPrevMonth = maxDayOfPrevMonth - weekDayOfFirstDay + 1
            for (day in startDayOfPrevMonth..maxDayOfPrevMonth) {
                val calendarDate = CalendarDate(day, hashValues.size % 7, calendar.getActualMaximum(Calendar.WEEK_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
                hashValues.add(calendarDate)
            }
            calendar.add(Calendar.MONTH, 1) // 다시 현재 달로 설정
            // 현재 달의 날짜들 추가
            val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (day in 1..maxDayOfMonth) {
                val calendarDate = CalendarDate(day, calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.WEEK_OF_MONTH), month + 1, year)
                hashValues.add(calendarDate)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            // 다시 이전 달의 마지막 날로 이동
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            // 마지막 날짜의 요일 확인
            calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth)
            val lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            // 마지막 날짜가 토요일이 아니라면 다음 달의 날짜들로 채움
            if (lastDayOfWeek != Calendar.SATURDAY) {
                val nextMonthDays = 7 - lastDayOfWeek // 토요일까지 필요한 날짜 수
                for (day in 1..nextMonthDays) {
                    val calendarDate = CalendarDate(day, calendar.get(Calendar.DAY_OF_WEEK), 1, (month + 2) % 12, if(month == 11) year + 1 else year)
                    hashValues.add(calendarDate)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                calendar.add(Calendar.MONTH, -1)
            }
            cacheCalendarList[hashKey] = hashValues
            return hashValues
        }
    }
}