package com.ing.ingterior.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.chat.ConversationListFragment
import com.ing.ingterior.ui.main.*
import com.ing.ingterior.ui.viewmodel.MainViewModel
import com.ing.ingterior.util.AnimationUtils
import com.ing.ingterior.util.StaticValues

class MainActivity : AppCompatActivity() {

        companion object{
        // 프래그먼트 생성을 지연시키기 위해 Pair 대신 Pair<() -> Fragment, Int> 사용
            private val pageMappings = mapOf(
                    R.id.menu_main to Pair({ HomeFragment() }, 0),
                    R.id.menu_site_management to Pair({ SiteListFragment() }, 1),
                    R.id.menu_message to Pair({ ConversationListFragment() }, 2),
            R.id.menu_setting to Pair({ SettingFragment() }, 3)
        )
    }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_main_splash_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_main_splash_layout) }
    private val bottomNavView: BottomNavigationView by lazy { findViewById(R.id.bottom_main_navigation_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        serverApi.updateConstruction(SiteModel(111, 27, "테스트 현장", 3)).enqueue(object : Callback<SiteModel> {
//            override fun onResponse(call: Call<SiteModel>, response: Response<SiteModel>) {
//                if (response.isSuccessful) {
//                    // 성공적으로 업데이트 되었을 때의 처리
//                    Log.d("test", "Update Success: ${response.body()}")
//                } else {
//                    // 서버 에러 처리
//                    Log.d("test","Server Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<SiteModel>, t: Throwable) {
//                // 네트워크 에러 처리
//                Log.d("test","Network Error: ${t.message}")
//            }
//        })
        StaticValues.updateDisplaySize(this)
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