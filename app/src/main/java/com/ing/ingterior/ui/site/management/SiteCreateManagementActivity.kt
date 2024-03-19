package com.ing.ingterior.ui.site.management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.ingterior.ui.CalendarDateAdapter
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.model.DateModel
import com.ing.ingterior.ui.ColorListAdapter
import com.ing.ingterior.ui.GridSpacingItemDecoration
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.getParcelableCompat
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

    private var gridColorItemDecoration: GridSpacingItemDecoration? = null
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
            layoutManager = GridLayoutManager(context, spanCount)
//            if (gridColorItemDecoration != null) removeItemDecoration(gridColorItemDecoration!!)
//            gridColorItemDecoration = GridSpacingItemDecoration(spanCount, resources.getDimensionPixelSize(R.dimen.page_horizontal_padding), false)
//            addItemDecoration(gridColorItemDecoration!!)
            adapter = ColorListAdapter(this@SiteCreateManagementActivity, itemSize)
        }
    }


    private fun moveCalendarByMonth(monthIncrement: Int) {
        calendar.add(Calendar.MONTH, monthIncrement)
        updateDateInView()
        setupRecyclerView()
        updateButtonsState()
    }

    private fun updateButtonsState() {
        // 한 달전까지만 이동 가능
        // 일 년전이지만 한 달전인 경우 ex) 현재 2024년 1월, 최소 이동 가능 2023년 12월
        val first = (currentCalendar.get(Calendar.YEAR) < calendar.get(Calendar.YEAR) && (currentCalendar.get(Calendar.MONTH) == 0 && calendar.get(Calendar.MONTH) == 11))
        val second = (currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && (currentCalendar.get(Calendar.MONTH) <= calendar.get(Calendar.MONTH)))
        Log.d("test", "updateButtonsState: first=$first, second=$second")
        // 같은 해면서 한 달전인 경우 ex) 현재 2024년 2월 최소 이동 가능 2024년 1월
        ibPrev.isEnabled = first || second

        // 현재 달로부터 6달 후를 계산
        val sixMonthsAheadCalendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 6)
        }

        ibNext.isEnabled = calendar.get(Calendar.YEAR) < sixMonthsAheadCalendar.get(Calendar.YEAR) ||
                (calendar.get(Calendar.YEAR) == sixMonthsAheadCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) < sixMonthsAheadCalendar.get(Calendar.MONTH))
    }

    private fun updateDateInView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
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

        calendarDateAdapter.update(days)
    }
}