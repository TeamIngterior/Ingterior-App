package com.ing.ingterior.ui.site

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.site.defects.SiteDefectsFragment
import com.ing.ingterior.ui.site.management.SiteManagementFragment
import com.ing.ingterior.ui.viewmodel.SiteViewModel
import com.ing.ingterior.ui.viewmodel.SiteViewModel.Companion.UI_DEFECTS_MODE
import com.ing.ingterior.util.getParcelableCompat
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.label.LabelView

class SiteActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "SiteActivity"
    }

    private lateinit var lbTitle: LabelView
    private lateinit var tabLayout: TabLayout
    private lateinit var fabAddPoint: FloatingActionButton
    private lateinit var ivBack: VisualImageButton

    private lateinit var siteViewModel: SiteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)

        siteViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java]
        moveDefectFragment()

        lbTitle = findViewById(R.id.lb_site_title)
        tabLayout = findViewById(R.id.tab_site_layout)
        fabAddPoint = findViewById(R.id.fab_site_add)

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