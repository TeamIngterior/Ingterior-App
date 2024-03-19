package com.ing.ingterior.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R

class ColorListAdapter(context: Context, private val itemSize: Int) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

    private val colorArray = context.resources.obtainTypedArray(R.array.palette)
    private var selectIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivColor = itemView.findViewById<ImageView>(R.id.iv_color_item)
        private val frameColor = itemView.findViewById<FrameLayout>(R.id.frame_color_item)

        fun bindView(position: Int, color: Int){
            ivColor.imageTintList = ColorStateList.valueOf(color)
            if(selectIndex == position){
                frameColor.setBackgroundResource(R.drawable.bg_select_color)
            }
            else{
                frameColor.setBackgroundResource(R.drawable.bg_unselect_color)
            }
            frameColor.setOnClickListener {
                selectIndex = position
                notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(itemSize, itemSize)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorListAdapter.ViewHolder, position: Int) {
        holder.bindView(position, colorArray.getColor(position, ContextCompat.getColor(holder.itemView.context, R.color.palette1)))
    }

    override fun getItemCount(): Int {
        return colorArray.length()
    }
}