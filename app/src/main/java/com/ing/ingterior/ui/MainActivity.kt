package com.ing.ingterior.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ing.ingterior.Logging
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.TYPE_GOOGLE
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.chat.ConversationListFragment
import com.ing.ingterior.ui.log.SignInActivity
import com.ing.ingterior.ui.main.*
import com.ing.ingterior.ui.viewmodel.HomeViewModel
import com.ing.ingterior.util.AnimationUtils
import com.ing.ingterior.util.StaticValues
import com.ing.ingterior.util.updateStatusBarColor

class MainActivity : AppCompatActivity() {

        companion object{
        // 프래그먼트 생성을 지연시키기 위해 Pair 대신 Pair<() -> Fragment, Int> 사용
            private val pageMappings = mapOf(
                    R.id.menu_main to Pair({ HomeFragment() }, 0),
                    R.id.menu_site_management to Pair({ HomeConstructionFragment() }, 1),
                    R.id.menu_message to Pair({ ConversationListFragment() }, 2),
            R.id.menu_setting to Pair({ SettingFragment() }, 3)
        )
    }

    private val viewModel : HomeViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[HomeViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_main_splash_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_main_splash_layout) }
    private val bottomNavView: BottomNavigationView by lazy { findViewById(R.id.bottom_main_navigation_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StaticValues.updateDisplaySize(this)
        if(Factory.get().getApplication().isFirst) {
            AnimationUtils.fadeInAndOut(ivLogo, 750, object : AnimationUtils.AnimationListener {
                override fun start() {
                    updateStatusBarColor(true)
                    lineSplashScreenLayout.isVisible = true
                }
                override fun end() {
                    lineSplashScreenLayout.isVisible = false
                    Factory.get().getApplication().isFirst = false
                    updateStatusBarColor(false)
                }
            })
        }
        else{
            lineSplashScreenLayout.isVisible = false

        }

        val user = User(id = 1111, email = "Test@gmail.com", nickName = "테스트 계정", token = "aaaaaa", type = TYPE_GOOGLE, avatarURL = "")
        Factory.get().getSession().setUser(user)
        bottomNavView.setOnItemSelectedListener { item ->
            val (fragmentCreator, pageIndex) = pageMappings[item.itemId] ?: return@setOnItemSelectedListener false
            val selectedFragment = fragmentCreator() // 프래그먼트 생성

            if(viewModel.currentPageIndex == pageIndex) return@setOnItemSelectedListener false

            supportFragmentManager.beginTransaction().replace(R.id.fragment_main_view, selectedFragment).commit()
            viewModel.currentPageIndex = pageIndex
            true
        }
    }

    fun selectBottomNavigationMenuItem(index: Int) {
        bottomNavView.selectedItemId = viewModel.getPageId(index)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(viewModel.fabOpen.value == true) {
            viewModel.fabOpen.postValue(false)
        }
        else{
            super.onBackPressed()
        }
    }
}