package com.ing.ingterior.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.EXTRA_MOVE_INDEX
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainActivity
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel
import com.ing.ingterior.ui.viewmodel.HomeViewModel
import com.ing.ingterior.util.Status
import com.ing.ingterior.util.updateStatusBarColor

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private lateinit var homeViewModel : HomeViewModel
    private lateinit var constructionViewModel: ConstructionViewModel
    private lateinit var lineSimpleEstimation: LinearLayout
    private lateinit var lineConstructionList :LinearLayout
    private lateinit var lineInteriorTip :LinearLayout

    private lateinit var tvUser: TextView

    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            if(result.data == null) return@registerForActivityResult
            val index = result.data!!.getIntExtra(EXTRA_MOVE_INDEX, homeViewModel.currentPageIndex)
            (requireActivity() as MainActivity).selectBottomNavigationMenuItem(index)
        }
    }

    private val updateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            homeViewModel.user.postValue(Factory.get().getSession().getUser())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =  ViewModelProvider(this, IngTeriorViewModelFactory())[HomeViewModel::class.java]
        constructionViewModel =  ViewModelProvider(this, IngTeriorViewModelFactory())[ConstructionViewModel::class.java]

        homeViewModel.currentPageIndex = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().updateStatusBarColor(false)
        bindingView(view)

        setListener()

        homeViewModel.user.observe(requireActivity()){
            tvUser.text = "반갑습니다! ${it.nickName}님"
        }

        constructionViewModel.getConstructionList(requireContext(), 111)
        constructionViewModel.constructionListData.observe(viewLifecycleOwner, Observer {
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

    private fun bindingView(view: View){
        lineSimpleEstimation = view.findViewById(R.id.line_home_simple_estimation)
        lineConstructionList = view.findViewById(R.id.line_home_main_site_list)
        lineInteriorTip = view.findViewById(R.id.line_home_tip)
        tvUser = view.findViewById(R.id.tv_home_user)

        lineSimpleEstimation.setOnClickListener{
            Factory.get().getMove().moveSimpleEstimationActivity(requireActivity())
        }

    }

    private fun setListener(){
        lineInteriorTip.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ingterior.tistory.com/"))
            startActivity(urlIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(Factory.get().getSession().getUser() != null) tvUser.text = "반갑습니다! ${Factory.get().getSession().getUser()?.nickName}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 상태바 색상을 원래대로 복원
        requireActivity().updateStatusBarColor(true) // 원래 색상의 리소스 ID
    }


}