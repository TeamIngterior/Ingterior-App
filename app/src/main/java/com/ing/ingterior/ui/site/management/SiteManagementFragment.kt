package com.ing.ingterior.ui.site.management

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.EventListAdapter
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.DayModel
import com.ing.ingterior.model.DummyModel
import com.ing.ingterior.model.MonthModel.Companion.calculateCurrentMonth
import com.ing.ingterior.ui.CalendarDateAdapter2
import com.ing.ingterior.ui.CalendarDateClickListener
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.StaticValues
import com.ing.ingterior.util.StaticValues.getSpecificMonth
import com.ing.ingterior.util.StaticValues.maxTime
import com.ing.ingterior.util.StaticValues.minTime
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.text.body.Body1View
import org.joda.time.LocalDate

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
    private lateinit var rvScheduleList: RecyclerView
    private lateinit var frameCalendarLayout: FrameLayout

    private var currentMonth = 0
    private var selectedDay:LocalDate = LocalDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        arguments?.let {
            siteViewModel.site = it.getParcelableCompat<Site>(EXTRA_SITE)
        }

        currentMonth = calculateCurrentMonth(LocalDate.now(), LocalDate().minusYears(1))
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

        rvScheduleList = view.findViewById(R.id.rv_site_management_schedule)
        val linearLayoutManager = object : LinearLayoutManager(requireContext()){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvScheduleList.layoutManager = linearLayoutManager

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
        currentMonth += monthIncrement
        val currentMonthModel = StaticValues.getMonthList()[currentMonth]
        val currentLocalDate = LocalDate(currentMonthModel.year, currentMonthModel.month, 1)
        logD(TAG, "moveCalendarByMonth: currentLocalDate=$currentLocalDate")
        ibPrev.isEnabled = !(currentLocalDate.year == minTime.year && currentLocalDate.monthOfYear == minTime.monthOfYear)
        ibNext.isEnabled = !(currentLocalDate.year == maxTime.year && currentLocalDate.monthOfYear == maxTime.monthOfYear)
        updateDateInView()
        setupRecyclerView()
        setUpScheduleView()
    }


    private fun updateDateInView() {
        tvYearMonth.text = StaticValues.getMonthList()[currentMonth].getCalendarTitle()
    }

    private fun setUpScheduleView(){
        (rvCalendarList.adapter as CalendarDateAdapter2).updateSchedules(DummyModel.getDummySchedule())
        rvScheduleList.adapter = EventListAdapter(DummyModel.getDummySchedule(), selectedDay!!, siteViewModel.site!!)
    }
    private fun setupRecyclerView() {
        val displaySize = StaticValues.displayPixelSize
        val displayWidth = displaySize.width
        val contentDisplayWidth = displayWidth - (2 * resources.getDimensionPixelSize(R.dimen.page_horizontal_padding))
        val itemWidth = contentDisplayWidth / 7
        rvCalendarList.adapter = CalendarDateAdapter2(this, getSpecificMonth(currentMonth), itemWidth)
        (rvCalendarList.adapter as CalendarDateAdapter2).notifySelectDate(-1, selectedDay!!)
    }

    override fun onCalendarItemClicked(position: Int, dayModel: DayModel) {
        selectedDay = LocalDate(dayModel.year, dayModel.month, dayModel.day)
        (rvCalendarList.adapter as CalendarDateAdapter2).notifySelectDate(position, selectedDay!!)
        setUpScheduleView()
    }


}