package com.ing.ingterior.model

import android.content.Context
import android.os.Parcelable
import com.ing.ingterior.util.StringFormatter.formatTimeStampString
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversationModel(var id: Long, var participantIds: String, var title: String, var text: String,
     var date:Long = 0, var unReadMessageCount: Int = 0): Parcelable {

    companion object{

    }


    fun getDateString(context: Context) = (formatTimeStampString(context, date, false))

    fun getRecipientList(limit: Int) : ArrayList<User> {
        val recipients = arrayListOf<User>()
        val ids = participantIds.split(" ").toTypedArray()
        var index = 0
        for(id in ids){
            try {
                recipients.add(User(id.toLong(), "AAA$id@gmail.com", "00$id", "aaaaa", "google", ""))
                index++
                if(index == limit) break
            }
            catch (e : NumberFormatException){
                continue
            }
        }
        return recipients
    }

}
