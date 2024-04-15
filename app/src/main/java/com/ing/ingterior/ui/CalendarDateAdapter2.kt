package com.ing.ingterior.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.model.DayModel
import com.ing.ingterior.model.EventModel
import com.ing.ingterior.model.MonthModel
import com.ing.ui.check.VisualCalendarTextView2
import org.joda.time.LocalDate

interface CalendarDateClickListener{
    fun onCalendarItemClicked(position: Int, dayModel: DayModel)
}
class CalendarDateAdapter2(private val clickListener: CalendarDateClickListener, private val monthModel: MonthModel, private val itemWidth: Int) : RecyclerView.Adapter<CalendarDateAdapter2.DayViewHolder>() {

    companion object {
        private const val TAG = "CalendarDateAdapter2"
    }

    private var selectedDay:LocalDate? = null
    private var scheduleList: ArrayList<EventModel> = arrayListOf()

    fun notifySelectDate(position: Int, localeDate: LocalDate){
        selectedDay = localeDate
        if(position < 0) notifyDataSetChanged()
        else notifyItemChanged(position)
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val vctDateView: VisualCalendarTextView2 = itemView.findViewById(R.id.vct_calendar_day2)
        private val lineDateParent: LinearLayout = itemView.findViewById(R.id.line_calendar_day_parent)
        private val lineScheduleLayout: LinearLayout = itemView.findViewById(R.id.line_calendar_day2_schedule)
        private val tvDateMore: TextView = itemView.findViewById(R.id.tv_calendar_day2_more)

        fun bind(position: Int, dayModel: DayModel) {
            vctDateView.setText(dayModel.day.toString())
            if(!dayModel.isEnable) {
                vctDateView.setDisable()
            }
            else {
                if(selectedDay?.year == dayModel.year && selectedDay?.monthOfYear == dayModel.month && selectedDay?.dayOfMonth == dayModel.day)
                    vctDateView.setSelect()
                else vctDateView.setDefault(dayModel.dayOfWeek)
            }

            lineDateParent.setOnClickListener {
                if(!dayModel.isEnable) return@setOnClickListener
                clickListener.onCalendarItemClicked(position, dayModel)
                notifyDataSetChanged()
            }

            lineScheduleLayout.removeAllViews()
            var moreCount = 0
            for(schedule in scheduleList){
                if(schedule.containCalendarDate(LocalDate(dayModel.year, dayModel.month, dayModel.day))) {
                    if(lineScheduleLayout.childCount == 3) {
                        moreCount++
                        continue
                    }
                    val circleView = ImageView(itemView.context)
                    val layoutParams = LinearLayout.LayoutParams(itemView.resources.getDimensionPixelSize(R.dimen.schedule_size), itemView.resources.getDimensionPixelSize(R.dimen.schedule_size))
                    if(lineScheduleLayout.childCount != 0) {
                        layoutParams.setMargins(4,0,0,0)
                    }
                    circleView.layoutParams = layoutParams
                    circleView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.bg_white_circle))
                    circleView.imageTintList = (ContextCompat.getColorStateList(itemView.context, schedule.palette))
                    lineScheduleLayout.addView(circleView)
                }
            }
            tvDateMore.isVisible = moreCount>0
            tvDateMore.text = "+${moreCount}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_2, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(itemWidth, parent.context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height))
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(position, monthModel.dayModelArrayList[position])
    }

    override fun getItemCount() = monthModel.dayModelArrayList.size


    fun updateSchedules(scheduleList: ArrayList<EventModel>) {
        this.scheduleList.clear()
        this.scheduleList.addAll(scheduleList)
        notifyDataSetChanged()
    }
}