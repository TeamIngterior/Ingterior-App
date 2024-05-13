package com.ing.ingterior.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.HomeViewModel
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel
import com.ing.ingterior.util.Status
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.button.VisualDotLineButton
import com.ing.ui.button.VisualImageButton

class HomeConstructionFragment : Fragment(), ConstructionItemListener {

    companion object{
        private const val TAG = "HomeConstructionFragment"
    }

    private lateinit var rvConstructionList: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var vdlbCreateSiteAction: VisualDotLineButton
    private lateinit var vibBack: VisualImageButton

    private lateinit var fabOption: FloatingActionButton
    private lateinit var vdbAddCode: VisualDefaultButton
    private lateinit var vdbAddNew: VisualDefaultButton
    private lateinit var lineOptions: LinearLayout

    private lateinit var homeViewModel : HomeViewModel
    private lateinit var constructionViewModel: ConstructionViewModel

    private lateinit var constructionListAdapter: ConstructionListAdapter
    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            Factory.get().getMove().moveSiteCreateOrEditActivity(requireActivity(), updateResultLauncher, null)
            fabOption.isVisible = Factory.get().getSession().isLogin()
            constructionViewModel.getConstructionList(requireContext(), 111)
        }
    }

    private val updateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        constructionViewModel.getConstructionList(requireContext(), 111)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =  ViewModelProvider(this, IngTeriorViewModelFactory())[HomeViewModel::class.java]
        constructionViewModel =  ViewModelProvider(this, IngTeriorViewModelFactory())[ConstructionViewModel::class.java]

        constructionListAdapter = ConstructionListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeViewModel.currentPageIndex = 1
        return inflater.inflate(R.layout.fragment_home_construction_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingView(view)

        bindingObserve()

        setListener()

        constructionViewModel.getConstructionList(requireContext(), 111)

        fabOption.isVisible = Factory.get().getSession().isLogin()

    }

    private fun bindingView(view: View){
        rvConstructionList = view.findViewById(R.id.rv_home_construction_list)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home_construction_list)
        vdlbCreateSiteAction = view.findViewById(R.id.vdlb_home_construction_list_new_action)
        vibBack = view.findViewById(R.id.vib_home_construction_list_back)

        fabOption = view.findViewById(R.id.fab_home_construction_list_option)
        vdbAddCode = view.findViewById(R.id.vdb_home_construction_list_add_code)
        vdbAddNew = view.findViewById(R.id.vdb_home_construction_list_add_new)
        lineOptions = view.findViewById(R.id.line_home_construction_list_options)

        rvConstructionList.adapter = constructionListAdapter
    }

    private fun bindingObserve(){
        homeViewModel.fabOpen.observe(viewLifecycleOwner) { isFabOpen ->
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

        constructionViewModel.constructionListData.observe(viewLifecycleOwner, Observer {
            val resource = it ?: return@Observer

            when(resource.status){
                Status.SUCCESS -> {
                    val constructions = resource.data as ArrayList<Construction> // 성공적으로 로드된 사이트 목록
                    logD(TAG, "getConstructionList: constructions=$constructions")
                    if(constructions.isEmpty()) {
                        vdlbCreateSiteAction.isVisible = true
                    }
                    else {
                        constructionListAdapter.updateAll(constructions)
                        vdlbCreateSiteAction.isVisible = false
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR_INT -> {
                    Toast.makeText(requireContext(), resource.intMsg!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
                else -> return@Observer
            }
        })

        constructionViewModel.likeRequestData.observe(viewLifecycleOwner, Observer {
            val resource = it ?: return@Observer

            when(resource.status){
                Status.SUCCESS -> {
                    logD(TAG, "getConstructionList: resource data=" + resource.data)
                }
                Status.ERROR_INT -> {
                    Toast.makeText(requireContext(), resource.intMsg!!, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
                else -> return@Observer
            }
        })
    }

    private fun setListener(){
        vdbAddCode.setOnClickListener {
            Factory.get().getMove().showAddCodeDialog(requireActivity())
        }

        vdbAddNew.setOnClickListener {
            Factory.get().getMove().moveSiteCreateOrEditActivity(requireActivity(), updateResultLauncher, null)
        }
        fabOption.setOnClickListener {
            homeViewModel.fabOpen.postValue(!homeViewModel.fabOpen.value!!)
        }

        vibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        vdlbCreateSiteAction.setOnClickListener {
            if(Factory.get().getSession().isLogin()) {
                Factory.get().getMove().moveSiteCreateOrEditActivity(requireActivity(), null, null)
            }
            else{
                Factory.get().getMove().moveSignInActivity(requireActivity(), moveResultLauncher)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            constructionViewModel.getConstructionList(requireContext(), 111)
        }
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.fabOpen.postValue(false)
    }

    override fun onSiteItemClicked(position: Int, construction: Construction) {
//        Factory.get().getMove().moveSiteActivity(requireActivity(), site)
    }

    override fun onFavoriteClicked(construction: Construction, isChecked: Boolean) {
//        updateFavoriteSite(requireContext(), site._id, isChecked)
        constructionViewModel.likeConstruction(requireContext(), 111, construction.constructionId)
    }

    override fun onEditClicked(position: Int, construction: Construction) {
//        Factory.get().getMove().moveSiteCreateOrEditActivity(requireActivity(), updateResultLauncher, site)
    }


}