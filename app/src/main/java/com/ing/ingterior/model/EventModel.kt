package com.ing.ingterior.model

import android.content.Context
import android.os.Parcelable
import com.ing.ingterior.R
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDate

@Parcelize
data class EventModel(val id: Long, val title: String, val content: String,
                      val start: Long, val end: Long, var palette: Int = R.color.palette1,
                      var durationTime: Long = 0L, var timeZone: String? = null, var noOfDayEvent: Int = 0,
                      var allDay: Boolean = true) :
    Parcelable {
    companion object{
        private const val TAG = "ScheduleModel"
    }
    fun containCalendarDate(currentLocale: LocalDate): Boolean{
        val startLocale = getLocaleDate(start)
        val endLocale = getLocaleDate(end)
        return currentLocale in startLocale..endLocale
    }


    fun getMarginTop(context: Context, showDateModel: CalendarDate): Int {
        return context.resources.getDimensionPixelSize(R.dimen.calendar_schedule_top_margin) +
                (showDateModel.week - 1) * context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height)
    }

    private fun getLocaleDate(milliSeconds: Long): LocalDate {
        val instantFromEpochMilli = Instant.ofEpochMilli(milliSeconds)
        return instantFromEpochMilli.toDateTime(DateTimeZone.getDefault()).toLocalDate()
    }
}