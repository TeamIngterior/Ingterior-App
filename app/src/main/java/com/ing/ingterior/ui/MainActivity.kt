package com.ing.ingterior.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.main.HomeFragment
import com.ing.ingterior.ui.main.MessageFragment
import com.ing.ingterior.ui.main.SettingFragment
import com.ing.ingterior.ui.main.SiteListFragment
import com.ing.ingterior.util.AnimationUtils

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_main_splash_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_main_splash_layout) }
    private val bottomNavView: BottomNavigationView by lazy { findViewById(R.id.bottom_main_navigation_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Factory.get().getApplication().isFirst) {
            AnimationUtils.fadeInAndOut(ivLogo, 750, object : AnimationUtils.AnimationListener {
                override fun start() {
                    lineSplashScreenLayout.isVisible = true
                }
                override fun end() {
                    lineSplashScreenLayout.isVisible = false
                    Factory.get().getApplication().isFirst = false
                }
            })
        }
        else{
            lineSplashScreenLayout.isVisible = false
        }

        bottomNavView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when(item.itemId) {
                R.id.menu_main -> {
                    selectedFragment = HomeFragment()
                }
                R.id.menu_site_management -> {
                    selectedFragment = SiteListFragment()
                }
                R.id.menu_message -> {
                    selectedFragment = MessageFragment()
                }
                R.id.menu_setting -> {
                    selectedFragment = SettingFragment()
                }
            }
            if(selectedFragment==null) false
            else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_view, selectedFragment)
                    .commit()
                true
            }
        }

    }
}