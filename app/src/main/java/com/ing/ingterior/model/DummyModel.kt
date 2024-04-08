package com.ing.ingterior.model

import com.ing.ingterior.R

object DummyModel {

    private const val oneDayMillis: Long = 24 * 60 * 60 * 1000
    fun getDummySchedule(): ArrayList<EventModel>{
        val schedules = ArrayList<EventModel>()
        schedules.add(
            EventModel(1, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis, System.currentTimeMillis(), R.color.palette1))
        schedules.add(
            EventModel(2, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis()+oneDayMillis*2, R.color.palette10))
        schedules.add(
            EventModel(3, "테스트", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis()+oneDayMillis*3, R.color.palette3))
        schedules.add(
            EventModel(4, "테스트2", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis(), R.color.palette4))
        schedules.add(
            EventModel(5, "테스트3", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis, R.color.palette5))
        schedules.add(
            EventModel(6, "테스트4", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*4, System.currentTimeMillis()-oneDayMillis*3, R.color.palette9))
        schedules.add(
            EventModel(7, "테스트5", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis, R.color.palette8))
        schedules.add(
            EventModel(8, "테스트6", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis*2, R.color.palette8))
        schedules.add(
            EventModel(9, "테스트7", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()+oneDayMillis*2, System.currentTimeMillis()+oneDayMillis*10, R.color.palette8))
        return schedules
    }
}