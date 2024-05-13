package com.ing.ingterior.ui.constructor.management

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
import com.ing.ingterior.cache.CalendarCache
import com.ing.ingterior.db.Site
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.ui.CalendarDateAdapter
import com.ing.ingterior.ui.ColorListAdapter
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.body.Body1View
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class SiteCreateManagementActivity : AppCompatActivity() {

    private lateinit var constructionViewModel: ConstructionViewModel
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

    private var threeMonthsAgo = 0L
    private var oneYearAfter = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_create_management)

        threeMonthsAgo = LocalDateTime.now().minusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        oneYearAfter = LocalDateTime.now().plusMonths(12).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        constructionViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[ConstructionViewModel::class.java]
        constructionViewModel.site = intent.getParcelableCompat<Site>(EXTRA_SITE)

        frameBlueprintLayout = findViewById(R.id.frame_site_create_management_blueprint_layout)
        frameBlueprintLayout.post {
            val params = frameBlueprintLayout.layoutParams
            params.height = frameBlueprintLayout.width // 너비와 같게 설정
            frameBlueprintLayout.layoutParams = params
        }
        ivBlueprint = findViewById(R.id.iv_site_create_management_blueprint)
        Glide.with(this).load(constructionViewModel.site?.imageLocation).into(ivBlueprint)
        calendarDateAdapter = CalendarDateAdapter()

        tvYearMonth = findViewById(R.id.tv_site_create_management_year_and_month)
        rvCalendarDayList = findViewById(R.id.rv_site_create_management_calendar)
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
        ibPrev = findViewById(R.id.ib_site_create_management_prev)
        ibNext = findViewById(R.id.ib_site_create_management_next)

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
        updateButtonsState()
    }

    private fun updateButtonsState() {
        Log.d("jypark", "updateButtonsState: threeMonthsAgo=$threeMonthsAgo")
        Log.d("jypark", "updateButtonsState: oneYearAfter=$oneYearAfter")
        Log.d("jypark", "updateButtonsState: calendar timeInMillis=${calendar.timeInMillis}")
        ibPrev.isEnabled = threeMonthsAgo < calendar.timeInMillis
        ibNext.isEnabled = oneYearAfter > calendar.timeInMillis
    }

    private fun updateDateInView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        tvYearMonth.text = "${year}년 ${month}월"
    }

    private fun setupRecyclerView() {
        val dates = mutableListOf<CalendarDate>()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val cday = calendar[Calendar.DAY_OF_MONTH]
        val week = calendar[Calendar.WEEK_OF_MONTH]
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val currentDateModel = CalendarDate(cday, dayOfWeek, week, month + 1, year)
        calendarDateAdapter.update(CalendarCache.getInstance().getCalendarDate(calendar), currentDateModel)
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