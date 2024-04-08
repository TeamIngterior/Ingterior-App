package com.ing.ingterior

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.CalendarDate
import com.ing.ingterior.model.EventModel

class EventListAdapter(private val scheduleList: ArrayList<EventModel>, private val showDate: CalendarDate, private val site:Site) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private val showScheduleList = arrayListOf<EventModel>()
    init {
        for(schedule in scheduleList){
            if(schedule.containCalendarDate(showDate)) {
                showScheduleList.add(schedule)
            }
        }
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lineParent = itemView.findViewById<LinearLayout>(R.id.line_site_event_parent_layout)
        private val viewTag = itemView.findViewById<View>(R.id.view_site_management_schedule_tag)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_title)
        private val tvContent = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_content)
        private val tvDate = itemView.findViewById<TextView>(R.id.tv_site_management_schedule_date)


        fun bind(event: EventModel){
            viewTag.backgroundTintList = ContextCompat.getColorStateList(itemView.context, event.palette)
            tvTitle.text = event.title
            tvContent.text = event.content
            tvDate.text = showDate.timeFormat2()
            lineParent.setOnClickListener {
                Factory.get().getMove().moveSiteEventActivity(itemView.context as Activity, event, site)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_site_management_event, parent, false))
    }

    override fun getItemCount(): Int {
        return showScheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(showScheduleList[position])
    }

}