package com.ing.ingterior.ui

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridColorItemDecoration(private val spanCount: Int, private val spacing: Int, private val edge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 아이템의 위치
        val column = position % spanCount // 아이템이 위치한 열

        if(position / spanCount == 0) {
            outRect.top = spacing
            outRect.bottom = spacing / 2
        }
        else {
            outRect.top = spacing / 2
            outRect.bottom = spacing / 2
        }

        if(column == 0) { // 가장 처음 열 아이템
            outRect.left = if(edge) spacing else 0
            outRect.right = spacing / 2
        }
        else if ((position + 1) % spanCount == 0) { // 가장 마지막 열 아이템
            outRect.left = spacing / 2
            outRect.right = if(edge) spacing else 0
        }
        else{
            outRect.left = spacing / 2
            outRect.right = spacing / 2
        }
    }
}
