package com.ing.ingterior.ui.site

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ing.ingterior.EXTRA_EVENT
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.EventModel
import com.ing.ingterior.util.StringFormatter.formatTimeStampString
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualImageButton
import java.text.SimpleDateFormat
import java.util.Locale

class SiteEventActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvContent: TextView
    private lateinit var ivOption: ImageView
    private lateinit var vibBack: VisualImageButton
    private lateinit var vibOption: VisualImageButton
    private lateinit var frameImageLayout: FrameLayout
    private lateinit var ivBluePrintView: ImageView
    private lateinit var rvImageList: RecyclerView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_event)

        tvTitle = findViewById(R.id.tv_site_event_title)

        tvDate = findViewById(R.id.tv_site_event_date)
        tvContent = findViewById(R.id.tv_site_event_content)
        ivOption = findViewById(R.id.iv_site_event_option)
        vibOption = findViewById(R.id.vib_site_event_option)
        frameImageLayout = findViewById(R.id.frame_site_event_layout)
        ivBluePrintView = findViewById(R.id.iv_site_event_blueprint)
        rvImageList = findViewById(R.id.rv_site_event_image_list)

        frameImageLayout.post {
            val params = frameImageLayout.layoutParams
            params.height = frameImageLayout.width // 너비와 같게 설정
            frameImageLayout.layoutParams = params
        }



        val event = intent.getParcelableCompat<EventModel>(EXTRA_EVENT)
        tvTitle.backgroundTintList = ContextCompat.getColorStateList(this, event?.palette ?: 0)
        tvTitle.text = event?.title
        tvContent.text = event?.content
        tvDate.text = formatTimeStampString(this, System.currentTimeMillis(), false)

        setDrawerLayout()

    }


    fun setDrawerLayout(){
        val site = intent.getParcelableCompat<Site>(EXTRA_SITE)
        drawerLayout = findViewById(R.id.drawer_site_event_layout)
        val ivNavClose = findViewById<ImageView>(R.id.iv_nav_site_close)
        val tvNavTitle = findViewById<TextView>(R.id.tv_nav_site_title)
        val tvNavCreator = findViewById<TextView>(R.id.tv_nav_site_creator)
        val tvNavDate = findViewById<TextView>(R.id.tv_nav_site_date)
        val tvNavCode = findViewById<TextView>(R.id.tv_nav_site_code)
        tvNavCode.text = site?.siteCode
        tvNavTitle.text = site?.siteName
        tvNavCreator.text = Factory.get().getSession().getUser()?.nickName
        tvNavDate.text = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(site?.createdDate)
        ivNavClose.setOnClickListener {
            drawerLayout.closeDrawers()
        }
        val rvNavParticipants = findViewById<RecyclerView>(R.id.rv_nav_site_participants)
        rvNavParticipants.layoutManager = LinearLayoutManager(this)
        rvNavParticipants.adapter = ParticipantAdapter(arrayListOf(Factory.get().getSession().getUser()!!))
        vibBack = findViewById(R.id.vib_site_event_back)
        vibBack.setOnClickListener {
            onBackPressed()
        }
        vibOption = findViewById(R.id.vib_site_event_option)
        vibOption.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }
}