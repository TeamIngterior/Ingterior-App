package com.ing.ingterior.util

import android.content.Context
import android.graphics.Typeface
import android.icu.util.Calendar
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.ing.ingterior.R
import java.text.SimpleDateFormat
import java.util.*

object StringFormatter {
    const val TAG = "StringFormat"
    const val DATE_SEC = (24*60*60*1000).toLong()
    const val YEAR_SEC = DATE_SEC * 365


    fun formatTimeStampString(context: Context, time: Long, fullFormat: Boolean): String {
        var formatFlags = DateUtils.FORMAT_ABBREV_ALL
        formatFlags = if (fullFormat) {
            formatFlags or (DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)
        } else {
            val sameYear = isSameYear(time)
            val sameDay = isSameDay(time)
            if (sameDay) {
                formatFlags or DateUtils.FORMAT_SHOW_TIME
            } else if (sameYear) {
                formatFlags or DateUtils.FORMAT_SHOW_DATE
            } else {
                formatFlags or (DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE)
            }
        }
        return DateUtils.formatDateTime(context, time, formatFlags)
    }
    fun formatTimeStampString(time: Long, fullFormat: Boolean): String {
        val calendarTime = Calendar.getInstance().apply { timeInMillis = time }
        val now = Calendar.getInstance()

        val format: String = when {
            fullFormat -> "yyyy.MM.dd"
            calendarTime.get(Calendar.YEAR) == now.get(Calendar.YEAR) -> {
                if (calendarTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
                    "HH:mm"
                } else {
                    "MM.dd"
                }
            }
            else -> "yyyy.MM.dd"
        }

        return SimpleDateFormat(format, Locale.getDefault()).format(calendarTime.time)
    }

    fun formatTimeString(context: Context?, time: Long): String {
        val formatFlags = DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_TIME
        return DateUtils.formatDateTime(context, time, formatFlags)
    }

    fun formatDateString(context: Context?, time: Long): String {
        val formatFlags = DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE
        return DateUtils.formatDateTime(context, time, formatFlags)
    }

    /*
    * @param time The unit is a millisecond
     */
    fun isSameDay(otherDateMillis: Long): Boolean{
        val currentDateMillis = System.currentTimeMillis()
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date(currentDateMillis)

        val otherCalendar = Calendar.getInstance()
        otherCalendar.time = Date(otherDateMillis)
        val diffDay = kotlin.math.abs(currentCalendar.get(Calendar.DAY_OF_YEAR) - otherCalendar.get(
            Calendar.DAY_OF_YEAR))
        return diffDay == 0
    }

    fun isSameYear(otherDateMillis: Long): Boolean{
        val currentDateMillis = System.currentTimeMillis()
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date(currentDateMillis)

        val otherCalendar = Calendar.getInstance()
        otherCalendar.time = Date(otherDateMillis)
        val diffYear = kotlin.math.abs(currentCalendar.get(Calendar.YEAR) - otherCalendar.get(
            Calendar.YEAR))
        return diffYear == 0
    }

    fun getPicSuffix(pic_path: String?): String {
        return if (pic_path == null || pic_path.indexOf(".") == -1) {
            ""
        } else pic_path.substring(pic_path.lastIndexOf(".") + 1).trim { it <= ' ' }.lowercase(
            Locale.getDefault()
        )
    }

    fun trimV4AddrZeros(addr: String?): String {
        if (addr == null) return ""
        val octets = addr.split("\\.").toTypedArray()
        if (octets.size != 4) return addr
        val builder = StringBuilder(16)
        var result: String? = null
        for (i in 0..3) {
            try {
                if (octets[i].length > 3) return addr
                builder.append(octets[i].toInt())
            } catch (e: NumberFormatException) {
                return addr
            }
            if (i < 3) builder.append('.')
        }
        result = builder.toString()
        return result
    }


    fun genHighlightSearchText(
        context: Context,
        constraint: String,
        nameLength: Int,
        name: String,
        addressLength: Int,
        address: String
    ): Array<CharSequence?> {
        val styledResults = arrayOfNulls<CharSequence>(2)
        styledResults[0] = name
        styledResults[1] = address
        if (isAllWhitespace(constraint)) {
            return styledResults
        }
        var foundMatch = false
        for (i in styledResults.indices) {
            if (styledResults[i] == null) continue
            val result = styledResults[i].toString()
            if (!foundMatch) {
                var index = -1
                index = if (i == 0) result.substring(0, nameLength).lowercase(Locale.getDefault())
                    .indexOf(
                        constraint.lowercase(
                            Locale.getDefault()
                        )
                    ) else result.substring(0, addressLength).lowercase(Locale.getDefault()).indexOf(
                    constraint.lowercase(
                        Locale.getDefault()
                    )
                )
                if (index != -1) {
                    val styled = SpannableStringBuilder.valueOf(result)
                    val highlightSpan = ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_color06
                        )
                    )
                    val boldSpan = StyleSpan(Typeface.BOLD)
                    styled.setSpan(
                        highlightSpan,
                        index,
                        index + constraint.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    styled.setSpan(
                        boldSpan,
                        index,
                        index + constraint.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    styledResults[i] = styled
                    foundMatch = true
                    continue
                }
            }
            styledResults[i] = result
        }
        return styledResults
    }

    private fun isAllWhitespace(string: String): Boolean {
        if (TextUtils.isEmpty(string)) {
            return true
        }
        for (element in string) {
            if (!Character.isWhitespace(element)) {
                return false
            }
        }
        return true
    }

    const val HOUR = 60 * 60 * 1000 // 3600000
    const val MINUTE = 60 * 1000 // 60000
    const val SECOND = 100 * 10 // 1000

    fun calDuration(context: Context, secs: Long): String {
        val isShort = secs < 3600
        val i_hour = secs / 60 % 60
        val i_min = (secs-(i_hour*3600)) / 60
        val i_sec = (secs-(i_hour*3600)-(i_min*60))
        val s_sec = if(i_sec in 0..9) "0$i_sec" else if(i_sec < 0) "00" else i_sec.toString()
        val s_min = if(i_min in 0..9) "0$i_min" else if(i_min < 0) "00" else i_min.toString()
        val s_hour = if(i_hour in 0..9) "0$i_hour" else if(i_hour < 0) "00" else i_hour.toString()
        return if(isShort) context.getString(R.string.duration_short_format, s_min, s_sec)
        else context.getString(R.string.duration_long_format, s_hour, s_min, s_sec)
    }

    fun formatTimer(time: Long): String {
        val hour: Int = (time / HOUR).toInt()
        val minute: Int = ((time - hour * HOUR) / MINUTE).toInt()
        val second: Int = (time - hour * HOUR - minute * MINUTE).toInt() / SECOND
        return if (time < HOUR) { formatZero(minute) + ":" + formatZero(second)
        } else { formatZero(hour) + ":" + formatZero(minute) + ":" + formatZero(second) }
    }

    fun formatTimerForMilliSecond(time: Long): String {
        val hour: Int = (time / HOUR).toInt()
        val minute: Int = ((time - hour * HOUR) / MINUTE).toInt()
        val second: Int = (time - hour * HOUR - minute * MINUTE).toInt() / SECOND
        val millisecond: Int = (time - hour * HOUR - minute * MINUTE - second * SECOND).toInt() /*/MILLISECOND*/
        return formatZero(minute) + ":" + formatZero(second) + ":" + formatMilliZero(millisecond)
    }

    private fun formatZero(t: Int): String {
        var result = ""
        if (t < 10) result += "0$t" else result += t
        return result
    }

    private fun formatMilliZero(t: Int): String {
        var result = ""
        if (t < 10) result += "00$t" else if (t < 100) result += "0$t" else result += t
        return result
    }
}