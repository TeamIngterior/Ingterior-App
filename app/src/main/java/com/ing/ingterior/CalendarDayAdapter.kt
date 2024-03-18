package com.ing.ingterior

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.model.DateModel
import com.ing.ui.check.VisualTextCheckBox

class CalendarDayAdapter(private val days: List<DateModel>) : RecyclerView.Adapter<CalendarDayAdapter.DayViewHolder>() {

    companion object {
        private const val TAG = "CalendarDayAdapter"
        const val SUNDAY = 0
        const val SATURDAY = 6

    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: VisualTextCheckBox = itemView as VisualTextCheckBox

        fun bind(dateModel: DateModel) {
            dayText.isVisible = dateModel.day != 0
            Log.d(TAG, "bind: day=$dateModel")
            dayText.setText(dateModel.day.toString())
            if(dateModel.week == SUNDAY) {
                dayText.setTextColor(ContextCompat.getColor(itemView.context, com.ing.ui.R.color.error_color))
            }
            else if(dateModel.week == SATURDAY) {
                dayText.setTextColor(ContextCompat.getColor(itemView.context, com.ing.ui.R.color.blue_color04))
            }
            else {
                dayText.setTextColor(ContextCompat.getColor(itemView.context, com.ing.ui.R.color.text_color_06))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size
}