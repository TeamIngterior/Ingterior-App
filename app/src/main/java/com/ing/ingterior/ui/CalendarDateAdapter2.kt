package com.ing.ingterior.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.model.ScheduleModel
import com.ing.ui.check.VisualCalendarTextView2

interface CalendarDateClickListener{
    fun onCalendarItemClicked(position: Int, calendarDate: CalendarDate)
}
class CalendarDateAdapter2(private val clickListener: CalendarDateClickListener, private val dates: ArrayList<CalendarDate>, private val nowCalendarModel: CalendarDate, private val itemWidth: Int) : RecyclerView.Adapter<CalendarDateAdapter2.DayViewHolder>() {

    companion object {
        private const val TAG = "CalendarDateAdapter2"
    }

    private var selectedDate:CalendarDate? = null
    private var scheduleList: ArrayList<ScheduleModel> = arrayListOf()

    fun notifySelectDate(position: Int, date: CalendarDate){
        selectedDate = date
        if(position < 0) notifyDataSetChanged()
        else notifyItemChanged(position)
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val vctDateView: VisualCalendarTextView2 = itemView.findViewById(R.id.vct_calendar_day2)
        private val lineDateParent: LinearLayout = itemView.findViewById(R.id.line_calendar_day_parent)
        private val lineScheduleLayout: LinearLayout = itemView.findViewById(R.id.line_calendar_day2_schedule)

        fun bind(position: Int, dateModel: CalendarDate) {
            vctDateView.setText(dateModel.day.toString())
            val otherDayOfMonth = !dateModel.isSameYearAndMonth(nowCalendarModel)
            if(otherDayOfMonth){
                vctDateView.setDisable()
            }
            else if(selectedDate == dateModel) vctDateView.setSelect()
            else vctDateView.setDefault(dateModel.dayOfWeek)

            lineDateParent.setOnClickListener {
                if(otherDayOfMonth) return@setOnClickListener
                clickListener.onCalendarItemClicked(position, dateModel)
                notifyDataSetChanged()
            }

            lineScheduleLayout.removeAllViews()
            for(schedule in scheduleList){
                logD(TAG, "bind: schedule=$schedule")
//                if(schedule.containCalendarDate(dateModel)) {
//                    val circleView = ImageView(itemView.context)
//                    circleView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.bg_white_circle))
//                    circleView.imageTintList = (ContextCompat.getColorStateList(itemView.context, schedule.palette))
//                    lineScheduleLayout.addView(circleView)
//                }
                val circleView = ImageView(itemView.context)
                val layoutParams = LinearLayout.LayoutParams(itemView.resources.getDimensionPixelSize(R.dimen.schedule_size), itemView.resources.getDimensionPixelSize(R.dimen.schedule_size))
                circleView.layoutParams = layoutParams
                circleView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.bg_white_circle))
                circleView.imageTintList = (ContextCompat.getColorStateList(itemView.context, schedule.palette))
                lineScheduleLayout.addView(circleView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_2, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(itemWidth, parent.context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height))
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(position, dates[position])
    }

    override fun getItemCount() = dates.size


    fun updateSchedules(scheduleList: ArrayList<ScheduleModel>) {
        this.scheduleList.clear()
        this.scheduleList.addAll(scheduleList)
        notifyDataSetChanged()
    }
}