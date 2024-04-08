package com.ing.ingterior.model

import android.content.Context
import android.os.Parcelable
import com.ing.ingterior.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(val id: Long, val title: String, val content: String, val startDate: Long, val endDate: Long, var palette: Int = R.color.palette1) :
    Parcelable {
    companion object{
        private const val TAG = "ScheduleModel"
    }
    fun containCalendarDate(calendarDate: CalendarDate): Boolean{
        return calendarDate.toMillis() in startDate..endDate
    }


    fun getMarginTop(context: Context, showDateModel: CalendarDate): Int {
        return context.resources.getDimensionPixelSize(R.dimen.calendar_schedule_top_margin) +
                (showDateModel.week - 1) * context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height)
    }

}