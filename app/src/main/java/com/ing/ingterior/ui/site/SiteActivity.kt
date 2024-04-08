package com.ing.ingterior.ui.site

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.site.defects.SiteDefectsFragment
import com.ing.ingterior.ui.site.management.SiteManagementFragment
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.label.LabelView
import java.text.SimpleDateFormat
import java.util.Locale


class SiteActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "SiteActivity"
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var lbTitle: LabelView
    private lateinit var tabLayout: TabLayout
    private lateinit var ivBack: VisualImageButton
    private lateinit var siteViewModel: SiteViewModel
    private lateinit var vibSiteOption: VisualImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        setDrawerLayout()

        moveDefectFragment()

        lbTitle = findViewById(R.id.lb_site_title)
        tabLayout = findViewById(R.id.tab_site_layout)

        ivBack = findViewById(R.id.vib_site_back)
        ivBack.setOnClickListener {
            finish()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "onTabSelected: ${tab.text}")
                when (tab.position) {
                    0 -> {
                        moveDefectFragment()
                    }
                    1 -> {
                        moveManagementFragment()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.d(TAG, "onTabReselected: ${tab.text}")
            }
        })

    }


    fun setDrawerLayout(){
        siteViewModel.site = intent.getParcelableCompat<Site>(EXTRA_SITE)
        val site = siteViewModel.site
        drawerLayout = findViewById(R.id.drawer_site_layout)
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

        vibSiteOption = findViewById(R.id.vib_site_option)
        vibSiteOption.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }
    fun setTitleCompat(title: String) {
        lbTitle.text = title
    }

    private fun moveDefectFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_site_content, SiteDefectsFragment.newInstance(intent.getParcelableCompat(EXTRA_SITE)))
        transaction.commit()
    }

    private fun moveManagementFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_site_content, SiteManagementFragment.newInstance(intent.getParcelableCompat(EXTRA_SITE)))
        transaction.commit()
    }

}