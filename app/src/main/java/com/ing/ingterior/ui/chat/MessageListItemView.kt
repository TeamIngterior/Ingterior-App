package com.ing.ingterior.ui.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.MessageModel
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.ContactAvatarDrawable
import com.ing.ingterior.ui.QuickAvatarImageView
import com.ing.ingterior.util.StringFormatter.formatDateString
import com.ing.ingterior.util.StringFormatter.formatTimeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MessageListItemView : LinearLayout {

    private lateinit var lineMessageDateLayout: LinearLayout
    private lateinit var tvMessageDate: TextView
    private lateinit var tvMessageBody: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var tvMessageTime: TextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        lineMessageDateLayout = findViewById(R.id.line_message_date_layout)
        tvMessageDate = findViewById(R.id.tv_message_date)
        tvMessageBody = findViewById(R.id.tv_message_body)
        ivAvatar = findViewById(R.id.iv_message_avatar)
        tvMessageTime = findViewById(R.id.tv_message_time)

    }

    fun bindMessage(messageModel: MessageModel){
        tvMessageBody.text = messageModel.body
        tvMessageTime.text = formatTimeString(context, messageModel.date)
        tvMessageDate.text = formatDateString(context, messageModel.date)

        if(Factory.get().getSession().getUser() != null) {
            setAvatar(Factory.get().getSession().getUser()!!, ivAvatar)
        }

        lineMessageDateLayout.isVisible = messageModel.isShowDate
        tvMessageTime.isVisible = messageModel.isShowTime
        ivAvatar.isVisible = messageModel.isShowAvatar
    }

    private fun setAvatar(user: User, imageView: ImageView) {
        if(user.avatarURL.isEmpty()) {
            val isMe = true
            if(isMe) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_user_avatar_white))
                imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_03))
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_user_avatar))
                imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

        }
        else{
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    val url = URL(user.avatarURL)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream: InputStream = connection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                }
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}