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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.util.StringFormatter
import com.ing.ui.button.VisualImageButton
import com.ing.ui.check.VisualImageCheckBox


interface SiteListItemListener{
    fun onSiteItemClicked(position: Int, site: Site)
    fun onFavoriteClicked(site: Site, isChecked: Boolean)
    fun onEditClicked(position: Int, site: Site)
}

class SiteListAdapter(private val clickListener: SiteListItemListener) : RecyclerView.Adapter<SiteListAdapter.SiteViewHolder>() {
    private val siteList: ArrayList<Site> = arrayListOf()

    fun updateAll(sites: ArrayList<Site>){
        siteList.clear()
        siteList.addAll(sites)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_site_list, parent, false)
        return SiteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiteViewHolder, position: Int) {
        val site = siteList[position]
        holder.bind(position, site)
    }

    override fun getItemCount(): Int = siteList.size

    inner class SiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDefaults: TextView = itemView.findViewById(R.id.tv_site_item_defaults_caption) // 하자체크
        private val tvManagement: TextView = itemView.findViewById(R.id.tv_site_item_management_caption) // 공사관리

        private val tvSiteName: TextView = itemView.findViewById(R.id.tv_site_item_name) // 현장 이름
        private val tvSiteCreator: TextView = itemView.findViewById(R.id.tv_site_item_creator) // 생성자 이름
        private val tvSiteCreatedDate: TextView = itemView.findViewById(R.id.tv_site_item_created_date) // 생성 날짜
        private val tvSiteCode: TextView = itemView.findViewById(R.id.tv_site_item_code) // 현장 코드
        private val lineSiteContent: LinearLayout = itemView.findViewById(R.id.line_site_item_content)

        private val vibOptions:VisualImageButton = itemView.findViewById(R.id.vib_site_item_option)
        private val vicFavorite: VisualImageCheckBox = itemView.findViewById(R.id.ivc_site_item_favorite)
        private val lineCopyText: LinearLayout = itemView.findViewById(R.id.line_site_item_code_share)

        private val popupView: View = LayoutInflater.from(itemView.context).inflate(R.layout.popup_site_item, null)



        fun bind(position: Int, site: Site) {
            tvDefaults.isVisible = site.defaultIds.isNotEmpty()
            tvManagement.isVisible = site.managementIds.isNotEmpty()

            tvSiteName.text = site.siteName
            tvSiteCreator.text = "생성자: " + Factory.get().getSession().getUser()?.email  // TODO "(생성자 아이디는 서버에서 가져와야 함)"
            tvSiteCreatedDate.text = StringFormatter.formatTimeStampString(site.createdDate, true)
            tvSiteCode.text = site.siteCode

            vicFavorite.setChecked(site.favorite)

            vicFavorite.setOnClickListener {
                val isCheck = !site.favorite
                site.favorite = isCheck
                vicFavorite.setChecked(isCheck)
                clickListener.onFavoriteClicked(site, isCheck)
            }

            lineSiteContent.setOnClickListener {
                clickListener.onSiteItemClicked(position, site)
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
                    clickListener.onEditClicked(position, site)
                }
                val lineDelete:LinearLayout = popupView.findViewById(R.id.line_popup_site_delete)
                lineDelete.setOnClickListener {

                }
            }
            lineCopyText.setOnClickListener {
                copyStringText(itemView.context, site.siteCode)
            }
        }
    }
    fun copyStringText(context: Context, text: String?){
        val clipboard = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text))
        Toast.makeText(context, "클립보드에 복사 했습니다.", Toast.LENGTH_SHORT).show()
    }
}