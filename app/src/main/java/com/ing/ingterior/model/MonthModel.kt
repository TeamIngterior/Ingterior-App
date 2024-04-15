package com.ing.ingterior.model

import com.ing.ingterior.Logging.logD
import com.ing.ingterior.util.StaticValues.maxTime
import com.ing.ingterior.util.StaticValues.minTime
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.Months
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.Locale

data class MonthModel(
    var monthNameStr: String = "",
    var month: Int = 0,
    var year: Int = 0,
    var noOfDay: Int = 0,
    var dayModelArrayList: ArrayList<DayModel> = arrayListOf(),
    var firstDayOfWeek: Int = 0
){

    companion object{
        private const val TAG = "MonthModel"

        fun getMonthList(allEventList: HashMap<LocalDate, ArrayList<EventModel>>) : ArrayList<MonthModel>{
            val resultList = ArrayList<MonthModel>()
            var minDateObj: DateTime = minTime.toDateTimeAtStartOfDay()
            val maxDateObj: DateTime = maxTime.toDateTimeAtStartOfDay()
            val months = Months.monthsBetween(minDateObj, maxDateObj).months
            val eventHash = HashMap<LocalDate, ArrayList<EventModel>>()
            for (i in 0 .. months) {
                var firstDayOfWeek = minDateObj.dayOfMonth().withMinimumValue().dayOfWeek().get()
                if (firstDayOfWeek == 7) firstDayOfWeek = 0
                val month = MonthModel()
                month.monthNameStr = (minDateObj.toString("MMMM"))
                month.year = minDateObj.year
                month.month = minDateObj.monthOfYear
                month.noOfDay = minDateObj.dayOfMonth().maximumValue
                month.firstDayOfWeek = firstDayOfWeek
                val currentYear = LocalDate().year
                val dayModelArrayList: ArrayList<DayModel> = ArrayList()
                var startDay = minDateObj.dayOfMonth().withMinimumValue().withTimeAtStartOfDay()
                var minWeek = startDay.dayOfWeek().withMinimumValue().toLocalDate().minusDays(1)
                while (minWeek < startDay.dayOfMonth().withMaximumValue().toLocalDate()) {
                    if (minWeek.monthOfYear == minWeek.plusDays(6).monthOfYear) {
                        val lastPattern = if (minWeek.year == currentYear) "d MMM" else "d MMM YYYY"
                        val str = arrayOf("tojigs" + minWeek.toString("d").uppercase(Locale.getDefault()) + " - "
                                + minWeek.plusDays(6).toString(lastPattern).uppercase(Locale.getDefault()))
                        if (!eventHash.containsKey(minWeek)) eventHash[minWeek] = arrayListOf(/*EventModel(eventTitles = str)*/)
                        minWeek = minWeek.plusWeeks(1)
                    } else {
                        val lastPattern = if (minWeek.year == currentYear) "d MMM" else "d MMM YYYY"
                        val str = arrayOf("tojigs" + minWeek.toString("d MMM").uppercase(Locale.getDefault()) + " - " +
                                minWeek.plusDays(6).toString(lastPattern).uppercase(Locale.getDefault()))
                        if (!eventHash.containsKey(minWeek)) eventHash[minWeek] = arrayListOf(/*EventModel(eventTitles = str)*/)
                        minWeek = minWeek.plusWeeks(1)
                    }
                }

                for (j in 1..month.noOfDay) {
                    val dayModel = DayModel()
                    dayModel.day = (startDay.dayOfMonth)
                    dayModel.month = (startDay.monthOfYear)
                    dayModel.year = (startDay.year)
                    dayModel.dayOfWeek = (startDay.dayOfWeek)
                    if (allEventList.containsKey(startDay.toLocalDate())) {
                        for(event in allEventList[startDay.toLocalDate()]!!){
                            val newEvent = event.copy()
                            val startMillis = startDay.toLocalDate().toDateTimeAtStartOfDay(
                                DateTimeZone.forID(newEvent.timeZone)).millis
                            val difference = newEvent.end - startMillis
                            logD(TAG, "getMonthList: newEvent=${newEvent.title}, difference=$difference")
                            if (difference > 86400000) {
                                val localDate1 = LocalDateTime(startMillis, DateTimeZone.forID(newEvent.timeZone)).withTime(0, 0, 0, 0)
                                val localDate2 = LocalDateTime(newEvent.end, DateTimeZone.forID(newEvent.timeZone)).withTime(23, 59, 59, 999)
                                val noOfDayEvent = Days.daysBetween(localDate1, localDate2).days
                                logD(TAG, "getMonthList: noOfDayEvent=${noOfDayEvent}")
                                newEvent.noOfDayEvent = noOfDayEvent
                                newEvent.allDay = true
                            }
                            else if (difference < 86400000) newEvent.noOfDayEvent = 0
                            else newEvent.noOfDayEvent = 1
                            dayModel.eventList.add(newEvent)
                        }
                        dayModel.hasEvent = true
                    }

                    if (startDay.toLocalDate() == LocalDate()) {
                        dayModel.isToday = (true)
                    } else {
                        dayModel.isToday = (false)
                    }
                    dayModel.isEnable = true
                    dayModelArrayList.add(dayModel)
                    startDay = startDay.plusDays(1)
                }
                month.dayModelArrayList = (dayModelArrayList)
                resultList.add(month)
                minDateObj = minDateObj.plusMonths(1)
            }
            return resultList
        }



        fun calculateCurrentMonth(currentMonthDay: LocalDate, minDate: LocalDate): Int {
            val minDateObj: LocalDate =
                minDate.toDateTimeAtStartOfDay().dayOfMonth().withMinimumValue().toLocalDate()
            val current = currentMonthDay.dayOfMonth().withMaximumValue()
            return Months.monthsBetween(minDateObj, current).months
        }
    }

    fun getCalendarTitle(): String {
        val localDate = LocalDate(year, month, 1)
        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy'년' MMMM").withLocale(Locale.KOREAN)
        return localDate.toString(fmt)
    }
}