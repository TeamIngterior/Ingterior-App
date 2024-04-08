package com.ing.ingterior.model

import android.content.Context
import com.ing.ingterior.util.StringFormatter.formatTimeStampString

data class Conversation(var id: Long,
                        var participantIds: String,
                        var snippet: String,
                        var text: String,
                        var date:Long = 0,
                        var unReadMessageCount: Int = 0,) {

    companion object{
        fun getDummyList() : ArrayList<Conversation>{
            val arrayList = arrayListOf<Conversation>()
            arrayList.add(Conversation(1,   "1", "가나다라마바사아자차카타파하가나다라마바사아자차카타파하","내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
                System.currentTimeMillis(), 2))
            arrayList.add(Conversation(2,  "1 2", "테스트2","내용내용내용내용내용내용",
                System.currentTimeMillis(), 2))
            arrayList.add(Conversation(3,   "1 2 3 4", "테스트3","내용내용내용내용",
                System.currentTimeMillis(), 2))
            arrayList.add(Conversation(3,   "1 2 4", "테스트3","내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
                System.currentTimeMillis(), 0))
            return arrayList
        }
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
