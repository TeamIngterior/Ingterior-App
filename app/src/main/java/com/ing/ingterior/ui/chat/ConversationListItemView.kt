package com.ing.ingterior.ui.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.model.ConversationModel
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.ContactAvatarDrawable
import com.ing.ingterior.ui.QuickAvatarImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


interface ConversationClickedListener{
    fun onConversationItemClicked(conversationModel: ConversationModel)
}
class ConversationListItemView : ConstraintLayout {
    companion object{
        private const val TAG = "ConversationListItemView"
    }
    var position = 0
    private var conversationModel: ConversationModel? = null
    private var activity: FragmentActivity? = null

    private lateinit var convItemLayout: ConversationListItemView
    private lateinit var tvSnippet: TextView
    private lateinit var tvBody: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvUnreadCount: TextView
    private lateinit var tvRecipientSize: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){}

    fun bind(position: Int, conversationModel: ConversationModel) {
        this.conversationModel = conversationModel

        updateViewContent()

        updateAvatarAndCheckView(false)
    }

    private var clickedListener: ConversationClickedListener? = null
    fun setListener(clickedListener: ConversationClickedListener){
        this.clickedListener = clickedListener
    }

    private fun updateViewContent(){
        if(conversationModel!=null) {
            val conv = conversationModel!!
            tvSnippet.text = conv.title
            tvBody.text = conv.text
            tvDate.text = conv.getDateString(context)
            tvUnreadCount.isVisible = conv.unReadMessageCount > 0
            tvUnreadCount.text = conv.unReadMessageCount.toString()

            convItemLayout.setOnClickListener {
                clickedListener?.onConversationItemClicked(conversationModel!!)
            }
        }


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

    private fun updateAvatarAndCheckView(isChecked: Boolean){
        if(conversationModel!=null) {
            val conv = conversationModel!!
            val recipients = conv.getRecipientList(4)
            val frameAvatar = findViewById<FrameLayout>(R.id.frame_conversation_item_avatar_parent)
            frameAvatar.removeAllViews()
            if(isChecked){
                val groupAvatar: ViewGroup = (LayoutInflater.from(context).inflate(R.layout.layout_group_avatar_checked, frameAvatar, false) as ViewGroup)
                frameAvatar.addView(groupAvatar)
            }
            else{
                val groupLayoutId = when(recipients.size){
                    1 -> {
                        R.layout.layout_chat_group_avatar_1
                    }
                    2 -> {
                        R.layout.layout_chat_group_avatar_2
                    }
                    3 -> {
                        R.layout.layout_chat_group_avatar_3
                    }
                    else -> { // 2023.10.20 jypark 1,2,3 나머지는 4 이상이라고 생각하고 layout_group_avatar_4로 가져가야 한다.
                        R.layout.layout_chat_group_avatar_4
                    }
                }
                val groupAvatar:ViewGroup = (LayoutInflater.from(context).inflate(groupLayoutId, frameAvatar, false) as ViewGroup)
                when(recipients.size){
                    1 -> {
                        val first = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_first)
                        logD(TAG, "recipients=${recipients[0]}, first=$first")
                        setAvatar(recipients[0], first)
                    }
                    2 -> {
                        val first = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_first)
                        val second = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_second)
                        setAvatar(recipients[0], first)
                        setAvatar(recipients[1], second)
                    }
                    3 -> {
                        val first = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_first)
                        val second = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_second)
                        val third = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_third)
                        setAvatar(recipients[0], first)
                        setAvatar(recipients[1], second)
                        setAvatar(recipients[2], third)
                    }
                    else -> {
                        val first = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_first)
                        val second = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_second)
                        val third = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_third)
                        val fourth = groupAvatar.findViewById<ImageView>(R.id.iv_avatar_fourth)
                        setAvatar(recipients[0], first)
                        setAvatar(recipients[1], second)
                        setAvatar(recipients[2], third)
                        setAvatar(recipients[3], fourth)
                    }
                }
                frameAvatar.addView(groupAvatar)
            }
            tvRecipientSize.text = recipients.size.toString()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        convItemLayout = findViewById(R.id.conversation_layout)
        tvSnippet = findViewById(R.id.tv_conversation_snippet)
        tvBody = findViewById(R.id.tv_conversation_body)
        tvDate = findViewById(R.id.tv_conversation_date)
        tvUnreadCount = findViewById(R.id.tv_conversation_unread_count)
        tvRecipientSize = findViewById(R.id.tv_conversation_recipient_size)
    }
}