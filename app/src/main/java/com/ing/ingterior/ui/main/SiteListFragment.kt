package com.ing.ingterior.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ing.ingterior.R
import com.ing.ingterior.db.Status
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainViewModel
import com.ing.ui.button.VisualImageButton

class SiteListFragment : Fragment() {

    companion object{
        private const val TAG = "SiteListFragment"
    }

    private val ivBack: VisualImageButton by lazy { requireView().findViewById(R.id.iv_site_list_back) }
    private val rvSiteList: RecyclerView by lazy { requireView().findViewById(R.id.rv_site_list) }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { requireView().findViewById(R.id.swipe_refresh_site_list) }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if(!Factory.get().getSession().isLogin()) Factory.get().getMove().moveSignInActivity(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel.currentPageIndex = 1
        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllSiteList(requireContext(), false)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllSiteList(requireContext(), true)
        }

        viewModel.allSiteListData.observe(requireActivity()) { status ->
            when (status) {
                is Status.Loading -> {
                    Log.d(TAG, "allSiteListData: Loading")
                    // 데이터 로딩 중, 로딩 인디케이터 표시
                    // 여기에 로딩 인디케이터를 표시하는 코드 작성
                }
                is Status.Success -> {
                    Log.d(TAG, "allSiteListData: Success")
                    // 데이터 로드 성공, UI 업데이트
                    val siteList = status.data // 성공적으로 로드된 사이트 목록
                    rvSiteList.adapter = SiteListAdapter(siteList)

                    swipeRefreshLayout.isRefreshing = false
                    // 여기에 사이트 목록을 UI에 표시하는 코드 작성
                }
                is Status.Error -> {
                    // 데이터 로드 실패, 에러 메시지 표시
                    val errorMessage = status.message // 에러 메시지
                    Log.d(TAG, "allSiteListData: Error: errorMessage=$errorMessage")
                    // 여기에 에러 메시지를 사용자에게 표시하는 코드 작성
                }
            }
        }
    }
}