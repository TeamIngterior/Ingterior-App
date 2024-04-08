package com.ing.ingterior.ui.chat

import android.content.Context
import android.database.Cursor
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.model.Conversation
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.QuickAvatarImageView
import com.ing.ingterior.ui.viewmodel.MainViewModel


class ConversationListItemView : ConstraintLayout {
    companion object{
        private const val TAG = "ConversationListItemView"
    }
    var position = 0
    private var conversation: Conversation? = null
    private var activity: FragmentActivity? = null

    private lateinit var tvSnippet: TextView
    private lateinit var tvBody: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvUnreadCount: TextView
    private lateinit var tvRecipientSize: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){}

    fun bind(position: Int, conversation: Conversation) {
        this.conversation = conversation

        updateViewContent()

        updateAvatarAndCheckView(false)
    }

    private fun updateViewContent(){
        if(conversation!=null) {
            val conv = conversation!!
            tvSnippet.text = conv.snippet
            tvBody.text = conv.text
            tvDate.text = conv.getDateString(context)
            tvUnreadCount.isVisible = conv.unReadMessageCount > 0
            tvUnreadCount.text = conv.unReadMessageCount.toString()
        }
    }

    private fun updateAvatarAndCheckView(isChecked: Boolean){
        if(conversation!=null) {
            val conv = conversation!!
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
                for(i in 0 until groupAvatar.childCount){
                    val quickContactImageView = groupAvatar.getChildAt(i) as QuickAvatarImageView
                    quickContactImageView.setGroupAvatarImageView(recipients[i], recipients.size)
                }
                frameAvatar.addView(groupAvatar)
            }
            tvRecipientSize.text = recipients.size.toString()
        }

    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        tvSnippet = findViewById(R.id.tv_conversation_snippet)
        tvBody = findViewById(R.id.tv_conversation_body)
        tvDate = findViewById(R.id.tv_conversation_date)
        tvUnreadCount = findViewById(R.id.tv_conversation_unread_count)
        tvRecipientSize = findViewById(R.id.tv_conversation_recipient_size)
    }
}