package com.ing.ingterior.model

import android.content.Context
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R

data class ScheduleModel(val id: Long, val title: String, val content: String, val startDate: CalendarDate, val endDate: CalendarDate, var palette: Int = R.color.palette1) {
    companion object{
        private const val TAG = "ScheduleModel"
    }
    fun containCalendarDate(calendarDate: CalendarDate): Boolean{
        return startDate.toMillis() <= calendarDate.toMillis() && calendarDate.toMillis() <= endDate.toMillis()
    }


    fun getMarginTop(context: Context, showDateModel: CalendarDate): Int {
        return context.resources.getDimensionPixelSize(R.dimen.calendar_schedule_top_margin) +
                (showDateModel.week - 1) * context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height)
    }

    fun getMarginStart(context: Context, itemWidth: Int, showDateModel: CalendarDate):Int {
        var marginStart = context.resources.getDimensionPixelSize(R.dimen.page_horizontal_padding)
        if(startDate.isSameWeek(showDateModel)) marginStart += itemWidth * startDate.dayOfWeek
        return marginStart
    }

    fun getMarginEnd(context: Context, itemWidth: Int, showDateModel: CalendarDate):Int {
        var marginEnd = context.resources.getDimensionPixelSize(R.dimen.page_horizontal_padding)
        if(endDate.isSameWeek(showDateModel)) marginEnd += itemWidth * (7 - endDate.dayOfWeek)
        return marginEnd
    }
}