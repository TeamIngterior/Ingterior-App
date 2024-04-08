package com.ing.ingterior.ui.site

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.R
import com.ing.ingterior.model.User

class ParticipantAdapter(private val participants: ArrayList<User>): RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_nav_site_participant, parent, false))
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    override fun onBindViewHolder(holder: ParticipantAdapter.ParticipantViewHolder, position: Int) {
        holder.bind(participants[position])
    }


    inner class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val lineParticipant: LinearLayout = itemView.findViewById(R.id.line_nav_site_participant)
            private val ivAvatar: ImageView = itemView.findViewById(R.id.iv_nav_site_participant_avatar)
            private val tvName: TextView = itemView.findViewById(R.id.iv_nav_site_participant_name)

            fun bind(user: User) {
                tvName.setText(user.nickName)
            }

        }
}