package com.ing.ingterior.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ing.ingterior.R
import com.ing.ingterior.db.Status
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainViewModel
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.button.VisualDotLineButton
import com.ing.ui.button.VisualImageButton

class SiteListFragment : Fragment() {

    companion object{
        private const val TAG = "SiteListFragment"
    }

    private lateinit var rvSiteList: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var vdlbNewAction: VisualDotLineButton
    private lateinit var vibBack: VisualImageButton

    private lateinit var fabOption: FloatingActionButton
    private lateinit var vdbAddCode: VisualDefaultButton
    private lateinit var vdbAddNew: VisualDefaultButton
    private lateinit var lineOptions: LinearLayout

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }

    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            Factory.get().getMove().moveNewSiteActivity(requireActivity(), updateResultLauncher)
            fabOption.isVisible = Factory.get().getSession().isLogin()
            viewModel.getAllSiteList(requireContext(), true)
        }
    }

    private val updateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.getAllSiteList(requireContext(), true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        rvSiteList = requireView().findViewById(R.id.rv_site_list)
        swipeRefreshLayout = requireView().findViewById(R.id.swipe_refresh_site_list)
        vdlbNewAction = requireView().findViewById(R.id.vdlb_site_list_new_action)
        vibBack = requireView().findViewById(R.id.vib_site_list_back)

        fabOption = requireView().findViewById(R.id.fab_site_list_option)
        vdbAddCode = requireView().findViewById(R.id.vdb_site_list_add_code)
        vdbAddNew = requireView().findViewById(R.id.vdb_site_list_add_new)
        lineOptions = requireView().findViewById(R.id.line_site_list_options)

        vdbAddCode.setOnClickListener {
            Factory.get().getMove().showAddCodeDialog(requireActivity())
        }

        vdbAddNew.setOnClickListener {
            Factory.get().getMove().moveNewSiteActivity(requireActivity(), updateResultLauncher)
        }

        viewModel.fabOpen.observe(viewLifecycleOwner) { isFabOpen ->
            if (isFabOpen) {
                lineOptions.visibility = View.VISIBLE
                // 열린 상태로 전환
                fabOption.animate().rotationBy(45f).setDuration(200).start()
                vdbAddCode.animate()
                    .alpha(1f) // 완전히 불투명하게
                    .setDuration(200) // 200ms 동안
                    .setListener(null) // 애니메이션 리스너 설정 (필요한 경우)

                vdbAddNew.animate()
                    .alpha(1f) // 완전히 불투명하게
                    .setDuration(200) // 200ms 동안
                    .setListener(null) // 애니메이션 리스너 설정 (필요한 경우)
            } else {
                // 닫힌 상태로 전환
                if(fabOption.rotation==0f) return@observe
                fabOption.animate().rotationBy(-45f).setDuration(200).start()
                // Fade Out 애니메이션
                vdbAddCode.animate()
                    .alpha(0f) // 완전히 투명하게
                    .setDuration(200) // 200ms 동안
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            lineOptions.visibility = View.GONE
                        }
                    })

                vdbAddNew.animate()
                    .alpha(0f) // 완전히 투명하게
                    .setDuration(200) // 200ms 동안
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            lineOptions.visibility = View.GONE
                        }
                    })
            }
        }


        fabOption.isVisible = Factory.get().getSession().isLogin()
        fabOption.setOnClickListener {
            viewModel.fabOpen.postValue(!viewModel.fabOpen.value!!)
        }

        vibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        vdlbNewAction.setOnClickListener {
            if(Factory.get().getSession().isLogin()) {
                Factory.get().getMove().moveNewSiteActivity(requireActivity())
            }
            else{
                Factory.get().getMove().moveSignInActivity(requireActivity(), moveResultLauncher)
            }
        }

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
                    rvSiteList.adapter = SiteListAdapter(requireActivity(), siteList)
                    swipeRefreshLayout.isRefreshing = false
                    vdlbNewAction.isVisible = false
                    // 여기에 사이트 목록을 UI에 표시하는 코드 작성
                }
                is Status.Error -> {
                    // 데이터 로드 실패, 에러 메시지 표시
                    val errorMessage = status.message // 에러 메시지
                    vdlbNewAction.isVisible = true
                    Log.d(TAG, "allSiteListData: Error: errorMessage=$errorMessage")
                    // 여기에 에러 메시지를 사용자에게 표시하는 코드 작성
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.fabOpen.postValue(false)
    }
}