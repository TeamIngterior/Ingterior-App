package com.ing.ingterior.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.ing.ingterior.R
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.Locale


data class DayModel(
    var month: Int = 0,
    var day: Int = 0,
    var year: Int = 0,
    var dayOfWeek: Int = 0,
    var isToday: Boolean = false,
    var isEnable: Boolean = false,
    var eventList: ArrayList<EventModel> = arrayListOf(),
    var noOfDayEvent: Int = 0,
    var selected: Boolean = false,
    var hasEvent: Boolean = false
) {

    override fun toString(): String {
        return "${year}년 ${month}월 ${day}일(${getDayOfWeekStr()}요일)"
    }

    private fun getDayOfWeekStr(): String{
        return when(dayOfWeek) {
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            7 -> "일"
            else -> "알 수 없음"
        }
    }

    fun getCalendarTitle(): String {
        val localDate = LocalDate(year, month, day)
        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy'년' MMMM").withLocale(Locale.KOREAN)
        return localDate.toString(fmt)
    }

    fun getDayColor(context: Context): Int {
        return if(dayOfWeek == 6) {
            ContextCompat.getColor(context, R.color.saturday_color)
        }
        else if(dayOfWeek == 7) {
            ContextCompat.getColor(context, R.color.sunday_color)
        }
        else {
            ContextCompat.getColor(context, R.color.text_color_06)
        }
    }

    fun durationDescendingSort(){
        val sortedEventList = eventList.sortedByDescending { it.durationTime }
        eventList.clear()
        eventList.addAll(sortedEventList)
    }

    fun getLocaleDate(): LocalDate{
        return LocalDate(year, month, day)
    }
}

