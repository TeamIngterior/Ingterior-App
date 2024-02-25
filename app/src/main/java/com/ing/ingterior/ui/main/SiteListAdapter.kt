package com.ing.ingterior.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.util.StringFormatter


class SiteListAdapter(private val siteList: ArrayList<Site>) : RecyclerView.Adapter<SiteListAdapter.SiteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_site_list, parent, false)
        return SiteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiteViewHolder, position: Int) {
        val site = siteList[position]
        holder.bind(site)
    }

    override fun getItemCount(): Int = siteList.size

    class SiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDefaults: TextView = itemView.findViewById(R.id.tv_site_item_defaults_caption) // 하자체크
        private val tvManagement: TextView = itemView.findViewById(R.id.tv_site_item_management_caption) // 공사관리

        private val tvSiteName: TextView = itemView.findViewById(R.id.tv_site_item_name) // 현장 이름
        private val tvSiteCreator: TextView = itemView.findViewById(R.id.tv_site_item_creator) // 생성자 이름
        private val tvSiteCreatedDate: TextView = itemView.findViewById(R.id.tv_site_item_created_date) // 생성 날짜
        private val tvSiteCode: TextView = itemView.findViewById(R.id.tv_site_item_code) // 현장 코드


        fun bind(site: Site) {
            tvDefaults.isVisible = site.defaultIds.isNotEmpty()
            tvManagement.isVisible = site.managementIds.isNotEmpty()

            tvSiteName.text = site.name
            tvSiteCreator.text = "생성자: " + site.creatorId.toString()
            tvSiteCreatedDate.text = StringFormatter.formatTimeStampString(site.createdDate, true)
            tvSiteCode.text = site.code

        }
    }
}