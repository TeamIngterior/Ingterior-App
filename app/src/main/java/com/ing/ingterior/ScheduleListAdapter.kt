package com.ing.ingterior

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.animation.expandHorizontally
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.model.ScheduleModel
import com.ing.ingterior.ui.CalendarDateAdapter2

class ScheduleListAdapter(private val scheduleList: ArrayList<ScheduleModel>, private val showDate: CalendarDate) : RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {

    private val showScheduleList = arrayListOf<ScheduleModel>()
    init {
        for(schedule in scheduleList){
            if(schedule.containCalendarDate(showDate)) {
                showScheduleList.add(schedule)
            }
        }
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewTag = itemView.findViewById<View>(R.id.view_site_management_schedule_tag)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_title)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_content)
        private val tvDate = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_date)


        fun bind(schedule: ScheduleModel){
            viewTag.backgroundTintList = ContextCompat.getColorStateList(itemView.context, schedule.palette)
            tvTitle.text = schedule.title
            tvContent.text = schedule.content
            tvDate.text = showDate.timeFormat2()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_site_management_schedule, parent, false))
    }

    override fun getItemCount(): Int {
        return showScheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(showScheduleList[position])
    }

}