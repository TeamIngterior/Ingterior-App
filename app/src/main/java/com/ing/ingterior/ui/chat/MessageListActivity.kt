package com.ing.ingterior.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.EXTRA_CONVERSATION
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.ConversationModel
import com.ing.ingterior.model.DummyModel
import com.ing.ingterior.model.DummyModel.DAY
import com.ing.ingterior.model.MessageModel
import com.ing.ingterior.ui.site.ParticipantAdapter
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ingterior.util.hideKeyboard
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.label.LabelView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageListActivity : AppCompatActivity() {

    companion object {

    }

    private var conversation: ConversationModel? = null
    private lateinit var vibBack: VisualImageButton
    private lateinit var lbTitle: LabelView
    private lateinit var ibNav: VisualImageButton
    private lateinit var rvMessageList: RecyclerView
    private lateinit var etInput: EditText
    private lateinit var ibSend: ImageButton
    private lateinit var drawerLayout: DrawerLayout

    private val messageList = DummyModel.getMessageList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        conversation = intent.getParcelableCompat(EXTRA_CONVERSATION)
        initViewBinding()
    }


    private fun initViewBinding(){
        vibBack = findViewById(R.id.vib_message_list_back)
        lbTitle = findViewById(R.id.lb_message_list_title)
        ibNav = findViewById(R.id.vib_message_list_nav)
        rvMessageList = findViewById(R.id.rv_message_list_view)
        etInput = findViewById(R.id.et_message_list_input)
        ibSend = findViewById(R.id.ib_message_list_send)

        vibBack.setOnClickListener {
            finish()
        }

        lbTitle.text = conversation?.title
        setDrawerLayout()

        rvMessageList.adapter = MessageListAdapter()
    }

    private fun setDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_message_list_layout)
        val ivNavClose = findViewById<ImageView>(R.id.iv_nav_close)
        val tvNavTitle = findViewById<TextView>(R.id.tv_nav_title)
        val tvNavCreator = findViewById<TextView>(R.id.tv_nav_creator)
        val tvNavDate = findViewById<TextView>(R.id.tv_nav_date)
        val tvNavCode = findViewById<TextView>(R.id.tv_nav_code)
        tvNavCode.text = "A001GA"
        tvNavTitle.text = conversation?.title
        tvNavCreator.text = Factory.get().getSession().getUser()?.nickName
        tvNavDate.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date().time - DAY)
        ivNavClose.setOnClickListener {
            drawerLayout.closeDrawers()
        }
        val rvNavParticipants = findViewById<RecyclerView>(R.id.rv_nav_participants)
        rvNavParticipants.layoutManager = LinearLayoutManager(this)
        if(Factory.get().getSession().getUser() != null) {
            rvNavParticipants.adapter = ParticipantAdapter(arrayListOf(Factory.get().getSession().getUser()!!))
        }
        ibNav.setOnClickListener {
            etInput.hideKeyboard(this)
            drawerLayout.openDrawer(GravityCompat.END)
        }

        val tvSiteCheckup = findViewById<LinearLayout>(R.id.line_nav_site_checkup_download)
        val tvSiteListDownload = findViewById<LinearLayout>(R.id.line_nav_site_list_download)
        val tvGoMessage = findViewById<LinearLayout>(R.id.line_nav_site_go_message)

        tvSiteCheckup.isVisible = false
        tvSiteListDownload.isVisible = false
        tvGoMessage.isVisible = false
    }

    inner class MessageListAdapter : RecyclerView.Adapter<MessageItemViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return messageList[position].type
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
            return if(viewType == MessageModel.TYPE_SEND) MessageItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_send_message, parent, false))
            else MessageItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recieve_message, parent, false))
        }

        override fun getItemCount(): Int {
            return messageList.size
        }

        override fun onBindViewHolder(holder: MessageItemViewHolder, position: Int) {
            val itemView = holder.itemView as MessageListItemView
            itemView.bindMessage(messageList[position])
        }



    }

    inner class MessageItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}