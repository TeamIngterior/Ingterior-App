package com.ing.ingterior.util

import android.app.Activity
import android.util.Size
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.model.DayModel
import com.ing.ingterior.model.MonthModel
import org.joda.time.LocalDate


object StaticValues {
    private const val TAG = "StaticValues"
    const val NAVIGATION_YEAR     = 0
    const val NAVIGATION_MONTH    = 1
    const val NAVIGATION_WEEK     = 2
    const val NAVIGATION_DAY      = 3
    const val NAVIGATION_SCHEDULE = 4
    const val NAVIGATION_SETTING = 5

    const val DAY_MILLISECOND = 24 * 60 * 60 * 1000L

    val minTime: LocalDate = LocalDate().minusYears(1)
    val maxTime: LocalDate = LocalDate().plusYears(1)

    private val monthList : ArrayList<MonthModel> = arrayListOf()
    fun getMonthList(): ArrayList<MonthModel> {
        if(monthList.isEmpty()) {
            val newMonthList = MonthModel.getMonthList(hashMapOf())
            monthList.addAll(newMonthList)
        }
        return monthList
    }

    fun getSpecificMonth(currentMonth: Int): MonthModel {
        val monthModel = monthList[currentMonth]
        val dayList = monthModel.dayModelArrayList
        val lastDayOfWeek = LocalDate(monthModel.year, monthModel.month, monthModel.dayModelArrayList.size).dayOfWeek
        val firstDayOfWeek = monthModel.firstDayOfWeek
        val prevMonth = LocalDate(monthModel.year, monthModel.month, 1)
        val todayDate = LocalDate()
        logD(TAG, "getSpecificMonth: firstDayOfWeek=${monthModel.firstDayOfWeek}")
        logD(TAG, "getSpecificMonth: lastDayOfWeek=$lastDayOfWeek")
        val startIndex = monthModel.firstDayOfWeek
        val endIndex = 6 - lastDayOfWeek
        val calendarSize = monthModel.dayModelArrayList.size + startIndex + endIndex
        val adapterData: ArrayList<DayModel> = ArrayList(calendarSize)
        for (i in 0 until calendarSize) {
            if (i < firstDayOfWeek) {
                //prev month
                val localDate = prevMonth.minusDays(firstDayOfWeek - i)
                val dayModel = DayModel()
                if (localDate.isEqual(todayDate)) {
                    dayModel.isToday = true
                }
                dayModel.day = localDate.dayOfMonth
                dayModel.month = localDate.monthOfYear
                dayModel.year = localDate.year
                dayModel.dayOfWeek = localDate.dayOfWeek
                dayModel.isEnable = false
                dayModel.durationDescendingSort()
                adapterData.add(dayModel)
            } else if (i >= dayList.size + firstDayOfWeek) {
                //next month
                val localDate = prevMonth.plusDays(i - firstDayOfWeek)
                val dayModel = DayModel()
                if (localDate.isEqual(todayDate)) {
                    dayModel.isToday = true
                }
                dayModel.day = localDate.dayOfMonth
                dayModel.month = localDate.monthOfYear
                dayModel.year = localDate.year
                dayModel.dayOfWeek = localDate.dayOfWeek
                dayModel.isEnable = false
                adapterData.add(dayModel)
            } else {
                //current month
                val dayModel = dayList[i - firstDayOfWeek]
                adapterData.add(dayModel)
            }
        }
        val newMonthModel = monthModel.copy()
        newMonthModel.dayModelArrayList = adapterData
        return newMonthModel
    }

    fun updateMonthList(){
        monthList.clear()
        monthList.addAll(MonthModel.getMonthList(hashMapOf()))
    }

    var displayPixelSize: Size = Size(0,0)
    fun updateDisplaySize(activity: Activity){
        displayPixelSize = activity.getDisplayPixelSize()
    }

    fun getHorizontalPaddingDisplayWidth(activity: Activity): Int {
        updateDisplaySize(activity)
        return displayPixelSize.width - activity.resources.getDimensionPixelSize(R.dimen.popup_horizontal_padding)
    }

}