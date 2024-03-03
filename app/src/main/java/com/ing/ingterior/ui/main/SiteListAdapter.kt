package com.ing.ingterior.ui.main

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.db.Site.Companion.updateFavoriteSite
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.Move
import com.ing.ingterior.ui.site.NewSiteActivity
import com.ing.ingterior.ui.site.SiteActivity
import com.ing.ingterior.util.StringFormatter
import com.ing.ui.check.VisualImageCheckBox


class SiteListAdapter(private val activity: Activity, private val siteList: ArrayList<Site>) : RecyclerView.Adapter<SiteListAdapter.SiteViewHolder>() {

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


        private val vicFavorite: VisualImageCheckBox = itemView.findViewById(R.id.iv_site_item_favorite)

        fun bind(position: Int, site: Site) {
            tvDefaults.isVisible = site.defaultIds.isNotEmpty()
            tvManagement.isVisible = site.managementIds.isNotEmpty()

            tvSiteName.text = site.name
            tvSiteCreator.text = "생성자: " + Factory.get().getSession().getUser()?.email  // TODO "(생성자 아이디는 서버에서 가져와야 함)"
            tvSiteCreatedDate.text = StringFormatter.formatTimeStampString(site.createdDate, true)
            tvSiteCode.text = site.code

            vicFavorite.setChecked(site.favorite)

            vicFavorite.setOnClickListener {
                val isCheck = !site.favorite
                updateFavoriteSite(itemView.context, site._id, isCheck)
                site.favorite = isCheck
                vicFavorite.setChecked(isCheck)
            }

            lineSiteContent.setOnClickListener {
                Factory.get().getMove().moveSiteActivity(activity)
            }
        }
    }
}