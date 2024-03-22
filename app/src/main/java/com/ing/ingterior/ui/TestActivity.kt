package com.ing.ingterior.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.model.DateModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.pxToDp
import com.ing.ui.text.body.Body1View
import java.util.Calendar

class TestActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "TestActivity"
    }


    private lateinit var tvYearMonth: Body1View
    private lateinit var rvCalendarDayList: RecyclerView
    private lateinit var ibPrev: ImageButton
    private lateinit var ibNext: ImageButton

    private val calendar = Calendar.getInstance()
    private val currentCalendar = Calendar.getInstance() // 현재 날짜를 유지하는 캘린더
    private lateinit var calendarDateAdapter: CalendarDateAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val displaySize = this.getDisplayPixelSize()
        Log.d(TAG, "onCreate: displaySize=$displaySize")
        val displayWidth = displaySize.width
        Log.d(TAG, "onCreate: displayWidth=$displayWidth, displayWidth dp=${this.pxToDp(displayWidth)}")
        val contentDisplayWidth = displayWidth - (2 * resources.getDimensionPixelSize(R.dimen.page_horizontal_padding))
        Log.d(TAG, "onCreate: contentDisplayWidth=$contentDisplayWidth, contentDisplayWidth dp=${this.pxToDp(contentDisplayWidth)}")
        val contentItemWidth = contentDisplayWidth / 7
        Log.d(TAG, "onCreate: contentItemWidth=$contentItemWidth, contentItemWidth dp=${this.pxToDp(contentItemWidth)}") // 54
        calendarDateAdapter = CalendarDateAdapter2(contentItemWidth)
        tvYearMonth = findViewById(R.id.tv_year_month)
        rvCalendarDayList = findViewById(R.id.rv_calendar_day_list)
        val gridLayoutManager = GridLayoutManager(this, 7)


        rvCalendarDayList.layoutManager = gridLayoutManager // 주당 일수 설정
        rvCalendarDayList.adapter = calendarDateAdapter
        ibPrev = findViewById(R.id.ib_prev)
        ibNext = findViewById(R.id.ib_next)

        updateDateInView()
        setupRecyclerView()
        updateButtonsState()

        ibPrev.setOnClickListener {
            moveCalendarByMonth(-1)
        }

        ibNext.setOnClickListener {
            moveCalendarByMonth(1)
        }

    }

    private fun moveCalendarByMonth(monthIncrement: Int) {
        calendar.add(Calendar.MONTH, monthIncrement)
        updateDateInView()
        setupRecyclerView()
//        updateButtonsState()
    }

    private fun updateButtonsState() {
        // 세 달 전까지만 이동 가능
        // 일 년 전이지만 세 달전인 경우 ex) 현재 2024년 3월, 최소 이동 가능 2023년 12월
        val first = (currentCalendar.get(Calendar.YEAR) > calendar.get(Calendar.YEAR) && (calendar.get(
            Calendar.MONTH) - currentCalendar.get(Calendar.MONTH) >= 9))
        val second = (currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && (currentCalendar.get(
            Calendar.MONTH) - 3 <= calendar.get(Calendar.MONTH)))

        ibPrev.isEnabled = first || second

        // 현재 달로부터 1년 후
        val oneYearAheadCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 12)
        }

        ibNext.isEnabled = calendar.get(Calendar.YEAR) < oneYearAheadCalendar.get(Calendar.YEAR) ||
                (calendar.get(Calendar.YEAR) == oneYearAheadCalendar.get(Calendar.YEAR) && calendar.get(
                    Calendar.MONTH) < oneYearAheadCalendar.get(Calendar.MONTH))
    }

    private fun updateDateInView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        tvYearMonth.text = "${year}년 ${month}월"
    }

    private fun setupRecyclerView() {
        val dates = mutableListOf<DateModel>()
        val year = calendar.get(Calendar.YEAR) // 현재 연도
        val month = calendar.get(Calendar.MONTH) // 현재 월
        val cday = calendar[Calendar.DAY_OF_MONTH] // 현재 일
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK] // 요일 (1: 일요일, 2: 월요일, ..., 7: 토요일)
        val currentDateModel = DateModel(cday, dayOfWeek, (month + 1) % 12, year)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val weekDayOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1

        // 이전 달의 날짜들로 첫 주 채우기
        calendar.add(Calendar.MONTH, -1) // 이전 달로 설정
        val maxDayOfPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startDayOfPrevMonth = maxDayOfPrevMonth - weekDayOfFirstDay + 1
        for (day in startDayOfPrevMonth..maxDayOfPrevMonth) {
            dates.add(
                DateModel(day, dates.size % 7, calendar.get(Calendar.MONTH) + 1, calendar.get(
                    Calendar.YEAR))
            )
        }

        calendar.add(Calendar.MONTH, 1) // 다시 현재 달로 설정

        // 현재 달의 날짜들 추가
        val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..maxDayOfMonth) {
            dates.add(DateModel(day, dates.size % 7, month + 1, year))
        }

        // 마지막 날짜의 요일 확인
        calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth)
        val lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // 마지막 날짜가 토요일이 아니라면 다음 달의 날짜들로 채움
        if (lastDayOfWeek != Calendar.SATURDAY) {
            val nextMonthDays = 7 - lastDayOfWeek // 토요일까지 필요한 날짜 수
            for (day in 1..nextMonthDays) {
                dates.add(DateModel(day, dates.size % 7, (month + 2) % 12, if(month == 11) year + 1 else year))
            }
        }

        calendarDateAdapter.update(dates, currentDateModel)
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