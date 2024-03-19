package com.ing.ingterior.ui.site.defects

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ing.ingterior.R
import com.ing.ingterior.model.ImageModel
import com.ing.ingterior.ui.viewmodel.SiteViewModel

class SiteDefectImagesAdapter(private val siteViewModel: SiteViewModel, private val itemSize: Int) : RecyclerView.Adapter<SiteDefectImagesAdapter.ViewHolder>() {

    companion object{
        private const val TAG = "SiteDefectImagesAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SiteDefectImagesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_site_image, parent, false)
        view.layoutParams = RecyclerView.LayoutParams(itemSize, itemSize)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiteDefectImagesAdapter.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: imageModel=${siteViewModel.defectImages[position]}")
        holder.bindItem(siteViewModel.defectImages[position])
    }

    override fun getItemCount(): Int {
        return siteViewModel.defectImages.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val ivDefect = itemView.findViewById<ImageView>(R.id.iv_defect_image)
        private val ivRemove = itemView.findViewById<ImageView>(R.id.iv_defect_image_remove)

        fun bindItem(imageModel: ImageModel){
            Glide.with(itemView.context).load(imageModel.uri).into(ivDefect)

            ivRemove.setOnClickListener {
                siteViewModel.removeDefectImage(imageModel)
                notifyDataSetChanged()
            }
        }
    }
}