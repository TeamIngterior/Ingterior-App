package com.ing.ingterior.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.util.StringFormatter
import com.ing.ui.button.VisualImageButton
import com.ing.ui.check.VisualImageCheckBox


interface ConstructionItemListener{
    fun onSiteItemClicked(position: Int, construction: Construction)
    fun onFavoriteClicked(construction: Construction, isChecked: Boolean)
    fun onEditClicked(position: Int, construction: Construction)
}

class ConstructionListAdapter(private val clickListener: ConstructionItemListener) : RecyclerView.Adapter<ConstructionListAdapter.ConstructionViewHolder>() {
    private val constructionList: ArrayList<Construction> = arrayListOf()

    fun updateAll(constructionList: ArrayList<Construction>){
        this.constructionList.clear()
        this.constructionList.addAll(constructionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_site_list, parent, false)
        return ConstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConstructionViewHolder, position: Int) {
        val construction = constructionList[position]
        holder.bind(position, construction)
    }

    override fun getItemCount(): Int = constructionList.size

    inner class ConstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDefaults: TextView = itemView.findViewById(R.id.tv_construction_item_defaults_caption) // 하자체크
        private val tvManagement: TextView = itemView.findViewById(R.id.tv_construction_item_management_caption) // 공사관리

        private val tvConstructionName: TextView = itemView.findViewById(R.id.tv_construction_item_name) // 현장 이름
        private val tvConstructionCreator: TextView = itemView.findViewById(R.id.tv_construction_item_creator) // 생성자 이름
        private val tvConstructionCreatedDate: TextView = itemView.findViewById(R.id.tv_construction_item_created_date) // 생성 날짜
        private val tvConstructionCode: TextView = itemView.findViewById(R.id.tv_construction_item_code) // 현장 코드
        private val lineConstructionContent: LinearLayout = itemView.findViewById(R.id.line_construction_item_content)

        private val vibOptions:VisualImageButton = itemView.findViewById(R.id.vib_construction_item_option)
        private val vicFavorite: VisualImageCheckBox = itemView.findViewById(R.id.ivc_construction_item_favorite)
        private val lineCopyText: LinearLayout = itemView.findViewById(R.id.line_construction_item_code_share)

        private val popupView: View = LayoutInflater.from(itemView.context).inflate(R.layout.popup_construction_item, null)



        fun bind(position: Int, construction: Construction) {
//            tvDefaults.isVisible = construction.defectsIds.isNotEmpty()
//            tvManagement.isVisible = construction.managementIds.isNotEmpty()

            tvConstructionName.text = construction.constructionName
            tvConstructionCreator.text = "생성자: " + construction.memberCode  // TODO "(생성자 아이디는 서버에서 가져와야 함)"
            tvConstructionCreatedDate.text = StringFormatter.formatTimeStampString(construction.convertDateTimeToLong(), true)
            tvConstructionCode.text = construction.constructionCode

            vicFavorite.setChecked(construction.liked)

            vicFavorite.setOnClickListener {
                val isCheck = !construction.liked
                construction.liked = isCheck
                vicFavorite.setChecked(isCheck)
                clickListener.onFavoriteClicked(construction, isCheck)
            }

            lineConstructionContent.setOnClickListener {
                clickListener.onSiteItemClicked(position, construction)
            }

            vibOptions.setOnClickListener{ button ->
                // PopupWindow를 생성합니다.
                PopupWindow(popupView, itemView.context.resources.getDimensionPixelOffset(R.dimen.popup_item_width), ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    isFocusable = true
                    elevation = 4f
                    setBackgroundDrawable(ColorDrawable(Color.WHITE))
                    isOutsideTouchable = true
                    showAsDropDown(button, 0, 0, Gravity.END)
                }
                val lineEdit:LinearLayout = popupView.findViewById(R.id.line_popup_site_edit)
                lineEdit.setOnClickListener {
                    clickListener.onEditClicked(position, construction)
                }
                val lineDelete:LinearLayout = popupView.findViewById(R.id.line_popup_site_delete)
                lineDelete.setOnClickListener {

                }
            }
            lineCopyText.setOnClickListener {
                copyStringText(itemView.context, construction.constructionCode)
            }
        }
    }
    fun copyStringText(context: Context, text: String?){
        val clipboard = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text))
        Toast.makeText(context, "클립보드에 복사 했습니다.", Toast.LENGTH_SHORT).show()
    }
}