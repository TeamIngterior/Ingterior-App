package com.ing.ingterior.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.CalendarDayAdapter
import com.ing.ingterior.R
import com.ing.ingterior.model.DateModel
import com.ing.ui.text.body.Body1View
import java.util.Calendar

class TestActivity : AppCompatActivity() {

    private lateinit var tvYearMonth: Body1View
    private lateinit var rvCalendarDayList: RecyclerView

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        tvYearMonth = findViewById(R.id.tv_year_month)
        rvCalendarDayList = findViewById(R.id.rv_calendar_day_list)

        // 현재 연도와 월을 TextView에 설정
        updateDateInView()

        setupRecyclerView()


        com.google.api.services.calendar.Calendar

        "https://www.googleapis.com/calendar/v3/calendars/calendarId/events?key=YAIzaSyCr4yojNO4vDOOCObjo-ltTsTFWnkH2Puk"
        "https://www.googleapis.com/calendar/v3/calendars/ko%23holiday%40group.v.calendar.google.com/events?key="
    }

    private fun updateDateInView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 1을 더해줍니다.

        tvYearMonth.text = "${year}년 ${month}월"
    }

    private fun setupRecyclerView() {
        val days = mutableListOf<DateModel>()
        val year = calendar.get(Calendar.YEAR)     // 현재 연도
        val month = calendar.get(Calendar.MONTH)   // 현재 월 (Calendar.MONTH는 0부터 시작하므로 주의)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val weekDayOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1

        // 첫 번째 날이 시작하기 전까지 빈 DateModel 객체를 채움
        for (i in 0 until weekDayOfFirstDay) {
            days.add(DateModel(0, i, month, year))
        }

        // 현재 달의 날짜를 DateModel 객체 리스트에 추가
        val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..maxDayOfMonth) {
            days.add(DateModel(day, (weekDayOfFirstDay + day - 1) % 7, month, year))
        }

        rvCalendarDayList.layoutManager = GridLayoutManager(this, 7) // 주당 일수 설정
        rvCalendarDayList.adapter = CalendarDayAdapter(days) // 어댑터에 days 리스트 전달
    }


    /*
    300x300 크기의 이미지를 중앙에서 2배로 줌인 했을 때, 보여지는 사각형의 시작 위치와 종료 위치의 x, y 값의 행렬을 계산하겠습니다.

줌인 전에 전체 이미지(300x300)가 보여졌다면, 2배로 줌인한 후에는 이미지의 크기가 같게 보이는 화면 영역이 1/2로 축소됩니다. 이는 이미지의 중앙 부분만 확대해서 보이게 됨을 의미합니다.

    원본 이미지 크기: 300x300
    줌인 배율: 2배
    줌인 후 보여지는 이미지의 크기: 원본의 1/2

따라서, 확대된 이미지에서 중앙에 보여질 부분의 크기는 원본의 절반인 150x150 크기가 됩니다. 이미지가 정중앙에서 확대되므로, 보여지는 사각형의 시작 위치와 종료 위치는 다음과 같습니다:

    시작 위치: 이미지의 가로와 세로에서 각각 1/4 지점에 해당하는 (75, 75)
    종료 위치: 이미지의 가로와 세로에서 각각 3/4 지점에 해당하는 (225, 225)

즉, 2배로 줌인한 상태에서 화면에 보여지는 이미지의 영역은 좌상단 모서리가 (75, 75)이고, 우하단 모서리가 (225, 225)인 사각형 영역입니다. 이 영역은 이미지의 정중앙 부분을 확대해서 보여주는 결과를 나타냅니다.

     */

}