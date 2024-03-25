package com.ing.ingterior.ui.site.management

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.model.DateModel
import com.ing.ingterior.ui.CalendarDateAdapter
import com.ing.ingterior.ui.ColorListAdapter
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.body.Body1View
import java.util.Calendar

class SiteCreateManagementActivity : AppCompatActivity() {

    private lateinit var siteViewModel: SiteViewModel
    private lateinit var frameBlueprintLayout: FrameLayout
    private lateinit var ivBlueprint: ImageView

    private lateinit var tvYearMonth: Body1View
    private lateinit var rvCalendarDayList: RecyclerView
    private lateinit var ibPrev: ImageButton
    private lateinit var ibNext: ImageButton
    private lateinit var vibBack: VisualImageButton

    private var colorItemDecoration: ColorGridSpacingItemDecoration? = null
    private lateinit var rvColorList: RecyclerView

    private val calendar = Calendar.getInstance()
    private val currentCalendar = Calendar.getInstance() // 현재 날짜를 유지하는 캘린더
    private lateinit var calendarDateAdapter: CalendarDateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_create_management)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        siteViewModel.site = intent.getParcelableCompat<Site>(EXTRA_SITE)

        frameBlueprintLayout = findViewById(R.id.frame_site_create_management_blueprint_layout)
        frameBlueprintLayout.post {
            val params = frameBlueprintLayout.layoutParams
            params.height = frameBlueprintLayout.width // 너비와 같게 설정
            frameBlueprintLayout.layoutParams = params
        }
        ivBlueprint = findViewById(R.id.iv_site_create_management_blueprint)
        Glide.with(this).load(siteViewModel.site?.imageLocation).into(ivBlueprint)
        calendarDateAdapter = CalendarDateAdapter()

        tvYearMonth = findViewById(R.id.tv_year_month)
        rvCalendarDayList = findViewById(R.id.rv_calendar_day_list)
        val gridLayoutManager = object : GridLayoutManager(this, 7){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        vibBack = findViewById(R.id.vib_site_create_management_back)
        vibBack.setOnClickListener {
            finish()
        }

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


        rvColorList = findViewById(R.id.rv_site_create_management_color_list)

        val displaySize = this.getDisplayPixelSize()
        val spanCount = 5

        // 1080 - 16*6 / 5
        val itemSize = (displaySize.width - ((spanCount+1) * resources.getDimensionPixelSize(R.dimen.page_horizontal_padding))) / spanCount
        rvColorList.apply {
            if(colorItemDecoration!=null) {
                removeItemDecoration(colorItemDecoration!!)
            }
            colorItemDecoration = ColorGridSpacingItemDecoration(spanCount, resources.getDimensionPixelSize(R.dimen.grid_item_margin))
            addItemDecoration(colorItemDecoration!!)
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = ColorListAdapter(this@SiteCreateManagementActivity, itemSize)
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
        val first = (currentCalendar.get(Calendar.YEAR) > calendar.get(Calendar.YEAR) && (calendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH) >= 9))
        val second = (currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && (currentCalendar.get(Calendar.MONTH) - 3 <= calendar.get(Calendar.MONTH)))

        ibPrev.isEnabled = first || second

        // 현재 달로부터 1년 후
        val oneYearAheadCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 12)
        }

        ibNext.isEnabled = calendar.get(Calendar.YEAR) < oneYearAheadCalendar.get(Calendar.YEAR) ||
                (calendar.get(Calendar.YEAR) == oneYearAheadCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) < oneYearAheadCalendar.get(Calendar.MONTH))
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
            dates.add(DateModel(day, dates.size % 7, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)))
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

    inner class ColorGridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // 아이템의 위치
            if(position / spanCount == 0) {
                outRect.top = spacing
                outRect.bottom = spacing / 2
            }
            else {
                outRect.top = spacing / 2
                outRect.bottom = spacing / 2
            }
        }
    }
}