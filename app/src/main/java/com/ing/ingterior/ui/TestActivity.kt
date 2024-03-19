package com.ing.ingterior.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.util.getDisplayPixelSize

class TestActivity : AppCompatActivity() {

    private var gridColorItemDecoration: GridSpacingItemDecoration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val rvColorList = findViewById<RecyclerView>(R.id.rv_site_create_management_color_list)

        val displaySize = this.getDisplayPixelSize()
        val spanCount = 5
        val itemSize = (displaySize.width - ((spanCount+1) * resources.getDimensionPixelSize(R.dimen.page_horizontal_padding))) / spanCount
        rvColorList.apply {
            layoutManager = GridLayoutManager(context, spanCount)
//            if (gridColorItemDecoration != null) removeItemDecoration(gridColorItemDecoration!!)
//            gridColorItemDecoration = GridSpacingItemDecoration(5, resources.getDimensionPixelSize(R.dimen.page_horizontal_padding), true)
//            addItemDecoration(gridColorItemDecoration!!)
            adapter = ColorListAdapter(this@TestActivity, itemSize)
        }
    }


    /*
    300x300 크기의 이미지를 중앙에서 2배로 줌인 했을 때, 보여지는 사각형의 시작 위치와 종료 위치의 x, y 값의 행렬을 계산하겠습니다.

줌인 전에 전체 이미지(300x300)가 보여졌다면, 2배로 줌인한 후에는 이미지의 크기가 같게 보이는 화면 영역이 1/2로 축소됩니다. 이는 이미지의 중앙 부분만 확대해서 보이게 됨을 의미합니다.

    원본 이미지 크기: 300x300
    줌인 배율: 2배
    줌인 후 보여지는 이미지의 크기: 원본의 1/2

따라서, 확대된 이미지에서 중앙에 보여질 부분의 크기는 원본의 절반인 150x150 크기가 됩니다. 이미지가 정중앙에서 확대되므로, 보여지는 사각형의 시작 위치와 종료 위치는 다음과 같습니다:

    시작 위치: 이미지의 가로와 세로에서 각각 1/4 지점에 해당하는 (75, 75)
    종료 위치: 이미지의 가로와 세로에서 각각 3/4 지점에 해당하는 (225, 225)

즉, 2배로 줌인한 상태에서 화면에 보여지는 이미지의 영역은 좌상단 모서리가 (75, 75)이고, 우하단 모서리가 (225, 225)인 사각형 영역입니다. 이 영역은 이미지의 정중앙 부분을 확대해서 보여주는 결과를 나타냅니다.

     */

}