package com.ing.ingterior.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.model.DateModel
import com.ing.ingterior.ui.site.management.SiteCreateManagementActivity
import com.ing.ui.check.VisualCalendarTextView

class CalendarDateAdapter : RecyclerView.Adapter<CalendarDateAdapter.DayViewHolder>() {

    companion object {
        private const val TAG = "CalendarDayAdapter"
    }

    private val dates: ArrayList<DateModel> = arrayListOf()
    val selectedDate = arrayListOf<DateModel>()
    var startDate: DateModel? = null
    var endDate: DateModel? = null

    fun update(newDates: MutableList<DateModel>) {
        dates.clear()
        dates.addAll(newDates)
        notifyDataSetChanged()
    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vctDateView: VisualCalendarTextView = itemView.findViewById(R.id.vct_date_view)

        fun bind(dateModel: DateModel) {
            if(dateModel.day == 0) vctDateView.visibility = View.INVISIBLE
            else vctDateView.visibility = View.VISIBLE

            vctDateView.setText(dateModel.day.toString())
            if(startDate == dateModel){
                vctDateView.setStart()
            }
            else if(endDate == dateModel) {
                vctDateView.setEnd()
            }
            else if(dateModel.isCompare(startDate) == 1 && dateModel.isCompare(endDate) == -1) {
                vctDateView.setContains()
            }
            else{
                vctDateView.setDefault(dateModel.week)
            }

            vctDateView.setOnClickListener {
                val isChecked = selectedDate.contains(dateModel)
                Log.d(TAG, "bind: isChecked=$isChecked, dateModel=$dateModel")
                if(isChecked){
                    selectedDate.remove(dateModel)
                    if(selectedDate.isEmpty()) {
                        startDate = null
                        endDate = null
                    }
                    else {
                        if(startDate == dateModel) {
                            startDate = endDate
                        }
                        else {
                            endDate = startDate
                        }
                    }
                }
                else{
                    if(selectedDate.isEmpty()) {
                        selectedDate.add(dateModel)
                        startDate = dateModel
                        endDate = dateModel
                    }
                    else{
                        if(selectedDate.size == 2) {
                            val compareStart = dateModel.isCompare(startDate!!)
                            val compareEnd = dateModel.isCompare(endDate!!)
                            if(compareStart == -1){
                                selectedDate.remove(startDate!!)
                                selectedDate.add(0, dateModel)
                                startDate = dateModel
                            }
                            else if(compareEnd == 1) {
                                selectedDate.remove(endDate!!)
                                selectedDate.add(selectedDate.size, dateModel)
                                endDate = dateModel
                            }
                            else {
                                selectedDate.clear()
                                selectedDate.add(dateModel)
                                startDate = dateModel
                                endDate = dateModel
                            }
                        }
                        else if(selectedDate.size == 1){
                            Log.d(TAG, "bind: size is one, selectedDate=$selectedDate")
                            val compareStart = dateModel.isCompare(startDate!!)
                            val compareEnd = dateModel.isCompare(endDate!!)
                            if(compareStart == -1){
                                selectedDate.add(0, dateModel)
                                startDate = dateModel
                            }
                            else if(compareEnd == 1) {
                                selectedDate.add(selectedDate.size, dateModel)
                                endDate = dateModel
                            }
                        }
                        else {
                            selectedDate.add(dateModel)
                            startDate = dateModel
                            endDate = dateModel
                        }
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount() = dates.size
}