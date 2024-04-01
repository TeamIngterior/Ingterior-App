package com.ing.ingterior.ui.site.management

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.cache.CalendarCache
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.model.DummyModel
import com.ing.ingterior.ui.CalendarDateAdapter2
import com.ing.ingterior.ui.CalendarDateClickListener
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getDisplayPixelSize
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.text.body.Body1View
import java.util.Calendar

class SiteManagementFragment : Fragment(), CalendarDateClickListener {

    companion object {
        private const val TAG = "SiteDefectsFragment"

        @JvmStatic
        fun newInstance(site: Site?) =
            SiteManagementFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SITE, site)
                }
            }
    }

    private lateinit var siteViewModel: SiteViewModel
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var ibNext: ImageButton
    private lateinit var ibPrev: ImageButton
    private lateinit var tvYearMonth: Body1View
    private lateinit var rvCalendarList: RecyclerView
    private lateinit var frameCalendarLayout: FrameLayout

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        arguments?.let {
            siteViewModel.site = it.getParcelableCompat<Site>(EXTRA_SITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)

    }

    private fun initViewBinding(view: View){
        fabAdd = view.findViewById(R.id.fab_site_management_add)

        fabAdd.setOnClickListener {
            if(siteViewModel.site == null) {
                Toast.makeText(requireContext(), "에러 발생", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            else Factory.get().getMove().moveSiteInsertManagementActivity(requireActivity(), siteViewModel.site!!)
        }

        rvCalendarList = view.findViewById(R.id.rv_site_management_calendar)
        val gridLayoutManager = object : GridLayoutManager(requireContext(), 7){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        rvCalendarList.layoutManager = gridLayoutManager // 주당 일수 설정
        ibNext = view.findViewById(R.id.ib_site_management_next)
        ibPrev = view.findViewById(R.id.ib_site_management_prev)
        tvYearMonth = view.findViewById(R.id.tv_site_management_year_and_month)
        frameCalendarLayout = view.findViewById(R.id.frame_site_management_calendar_layout)

        ibPrev.setOnClickListener {
            moveCalendarByMonth(-1)
        }

        ibNext.setOnClickListener {
            moveCalendarByMonth(1)
        }

        updateDateInView()
        setupRecyclerView()
        setUpScheduleView()
    }


    private fun moveCalendarByMonth(monthIncrement: Int) {
        calendar.add(Calendar.MONTH, monthIncrement)
        updateDateInView()
        setupRecyclerView()
        setUpScheduleView()
    }


    private fun updateDateInView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        tvYearMonth.text = "${year}년 ${month}월"
    }

    private fun setUpScheduleView(){
        (rvCalendarList.adapter as CalendarDateAdapter2).updateSchedules(DummyModel.getDummySchedule())
    }


    private var selectedDate:CalendarDate? = null
    private fun setupRecyclerView() {
        val displaySize = requireActivity().getDisplayPixelSize()
        val displayWidth = displaySize.width
        val contentDisplayWidth = displayWidth - (2 * resources.getDimensionPixelSize(R.dimen.page_horizontal_padding))
        val itemWidth = contentDisplayWidth / 7

        val year = calendar.get(Calendar.YEAR) // 현재 연도
        val month = calendar.get(Calendar.MONTH) // 현재 월
        val showDateModel = CalendarDate(1, calendar[Calendar.DAY_OF_WEEK], calendar.get(Calendar.WEEK_OF_MONTH), month + 1 , year)
        selectedDate = showDateModel
        rvCalendarList.adapter = CalendarDateAdapter2(this, CalendarCache.getInstance().getCalendarDate(calendar), showDateModel, itemWidth)
        (rvCalendarList.adapter as CalendarDateAdapter2).notifySelectDate(-1, selectedDate!!)

    }

    override fun onCalendarItemClicked(position: Int, calendarDate: CalendarDate) {
        selectedDate = calendarDate
        (rvCalendarList.adapter as CalendarDateAdapter2).notifySelectDate(position, selectedDate!!)
    }


}