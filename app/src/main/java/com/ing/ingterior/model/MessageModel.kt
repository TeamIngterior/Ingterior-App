package com.ing.ingterior.model

import android.icu.util.Calendar
import android.provider.Telephony.BaseMmsColumns.MESSAGE_BOX_SENT
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.model.DummyModel.MINUTE
import java.util.Date
import kotlin.math.abs

data class MessageModel(
    val id: Long = -1L,
    val conversationId: Long = -1L,
    val recipient: String = "",
    val body: String = "",
    val date: Long = -1L,
    var type: Int = TYPE_SEND,
    var read: Int = 0,
    var isShowDate: Boolean = true,
    var isShowTime: Boolean = false,
    var isShowAvatar: Boolean = true,
){

    companion object{
        private const val TAG = "MessageModel"

        const val TYPE_RECEIVE = 0
        const val TYPE_SEND = 1


        fun showDateWithMessage(before: MessageModel, current: MessageModel): Boolean {
            return !before.isInSameDate(current.date)
        }

        fun canClusterWithMessage(before: MessageModel, current: MessageModel): Boolean {
            logD(TAG, "canClusterWithMessage: id=${current.id} beforeId=${before.id}")
            val isSameBox = before.type == current.type
            val isSameMinute = current.isSameMinute(before.date)
            logD(TAG, "canClusterWithMessage: isSameBox=${isSameBox} isSameMinute=${isSameMinute}")
            return (isSameBox && isSameMinute)
        }

    }



    private fun isInSameDate(otherDateMillis: Long): Boolean {
        val currentDateMillis = if (date == 0L) System.currentTimeMillis() else date
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date(currentDateMillis)

        val otherCalendar = Calendar.getInstance()
        otherCalendar.time = Date(otherDateMillis)
        val diffDay = abs(currentCalendar.get(Calendar.DAY_OF_YEAR) - otherCalendar.get(Calendar.DAY_OF_YEAR))
        return diffDay == 0
    }

    private fun isSameMinute(otherDateMillis: Long): Boolean {
        val currentDateMillis = if (date == 0L) System.currentTimeMillis() else date
        val diff = abs(currentDateMillis - otherDateMillis)
        return diff < MINUTE
    }


}
