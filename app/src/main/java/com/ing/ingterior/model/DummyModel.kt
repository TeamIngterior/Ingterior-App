package com.ing.ingterior.model

import com.ing.ingterior.R

object DummyModel {
    fun getDummySchedule(): ArrayList<ScheduleModel>{
        val schedules = ArrayList<ScheduleModel>()
        schedules.add(
            ScheduleModel(1, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(1, 1, 1, 4, 2024), CalendarDate(8, 1, 2,4, 2024), R.color.palette1))
        schedules.add(
            ScheduleModel(2, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(3, 3, 1, 4, 2024), CalendarDate(4, 4, 1, 4, 2024), R.color.palette10))
        schedules.add(
            ScheduleModel(3, "테스트", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(1, 1, 1,4, 2024), CalendarDate(2, 2, 1,4, 2024), R.color.palette3))
        schedules.add(
            ScheduleModel(4, "테스트2", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(2, 2, 1,4, 2024), CalendarDate(3, 3, 1,4, 2024), R.color.palette4))
        schedules.add(
            ScheduleModel(5, "테스트3", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(1, 1, 1,4, 2024), CalendarDate(1, 1, 1,4, 2024), R.color.palette5))
        schedules.add(
            ScheduleModel(6, "테스트4", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(1, 1, 1,4, 2024), CalendarDate(1, 1, 1,4, 2024), R.color.palette9))
        schedules.add(
            ScheduleModel(7, "테스트5", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(1, 1, 1,4, 2024), CalendarDate(1, 1, 1,4, 2024), R.color.palette8))
        schedules.add(
            ScheduleModel(8, "테스트6", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(3, 3, 1,4, 2024), CalendarDate(4, 4, 1,4, 2024), R.color.palette8))
        schedules.add(
            ScheduleModel(9, "테스트7", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                CalendarDate(3, 3, 1,4, 2024), CalendarDate(4, 4, 1,4, 2024), R.color.palette8))
        return schedules
    }
}