package com.ing.ingterior.ui.site

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ing.ingterior.R
import com.ing.ui.button.VisualDotLineButton

class SiteManagementActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var vdbAddPoint: VisualDotLineButton
    private lateinit var fabAddPoint: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_management)

        tabLayout = findViewById(R.id.tab_site_management_layout)
        fabAddPoint = findViewById(R.id.fab_site_management_add)
        vdbAddPoint = findViewById(R.id.vdb_site_management_add_point)
        vdbAddPoint.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_site_management_content, SiteInsertDefectsFragment())
            transaction.commit()
            vdbAddPoint.isVisible = false
            fabAddPoint.isVisible = false
            tabLayout.isVisible = false
        }
    }

    override fun onBackPressed() {
        if(!vdbAddPoint.isVisible) {
           vdbAddPoint.isVisible = true
            fabAddPoint.isVisible = true
            tabLayout.isVisible = true
        }
        else super.onBackPressed()
    }
}