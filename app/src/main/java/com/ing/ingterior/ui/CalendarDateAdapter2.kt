package com.ing.ingterior.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.model.DateModel
import com.ing.ui.check.VisualCalendarTextView2

class CalendarDateAdapter2(private val itemWidth: Int) : RecyclerView.Adapter<CalendarDateAdapter2.DayViewHolder>() {

    companion object {
        private const val TAG = "CalendarDateAdapter2"
    }

    private val dates: ArrayList<DateModel> = arrayListOf()
    var selectedDate:DateModel? = null
    private var currentDateModel: DateModel? = null

    fun update(newDates: MutableList<DateModel>, currentDateModel: DateModel) {
        this.currentDateModel = currentDateModel
        dates.clear()
        dates.addAll(newDates)
        notifyDataSetChanged()
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val vctDateView: VisualCalendarTextView2 = itemView.findViewById(R.id.vct_date_view)
        private val constDateParent: ConstraintLayout = itemView.findViewById(R.id.const_calendar_day_parent)

        fun bind(dateModel: DateModel) {
            if(dateModel.day == 0) vctDateView.visibility = View.INVISIBLE
            else vctDateView.visibility = View.VISIBLE

            vctDateView.setText(dateModel.day.toString())
            val otherDayofMonth = !dateModel.isSameYearAndMonth(currentDateModel)
            if(otherDayofMonth){
                vctDateView.setDisable()
            }
            else if(selectedDate == dateModel) {
                vctDateView.setSelect()
            }
            else vctDateView.setDefault(dateModel.week)

            constDateParent.setOnClickListener {
                if(otherDayofMonth) return@setOnClickListener
                val isChecked = selectedDate == dateModel
                Log.d(TAG, "bind: isChecked=$isChecked, dateModel=$dateModel")
                if(!isChecked){
                    selectedDate = dateModel
                }
                notifyDataSetChanged()
            }

            Log.d(TAG, "bind: constDateParent width=${constDateParent.measuredWidth}, height=${constDateParent.measuredHeight}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day_2, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(itemWidth, parent.context.resources.getDimensionPixelSize(com.ing.ui.R.dimen.calendar_item_height))
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount() = dates.size
}