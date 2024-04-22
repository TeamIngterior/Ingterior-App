package com.ing.ingterior.ui.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.EXTRA_MOVE_INDEX
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainActivity
import com.ing.ingterior.ui.viewmodel.MainViewModel
import com.ing.ingterior.util.updateStatusBarColor

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private lateinit var lineSimpleEstimation: LinearLayout
    private lateinit var lineSiteList :LinearLayout

    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            if(result.data == null) return@registerForActivityResult
            val index = result.data!!.getIntExtra(EXTRA_MOVE_INDEX, viewModel.currentPageIndex)
            (requireActivity() as MainActivity).selectBottomNavigationMenuItem(index)
        }
    }

    private val updateResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.user.postValue(Factory.get().getSession().getUser())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.currentPageIndex = 0
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
        lineSimpleEstimation = requireView().findViewById(R.id.line_home_simple_estimation)
        lineSiteList = requireView().findViewById(R.id.line_home_main_site_list)

        lineSimpleEstimation.setOnClickListener{
            Factory.get().getMove().moveSimpleEstimationActivity(requireActivity())
        }

//        lineSiteList.setOnClickListener{
//            if(Factory.get().getSession().isLogin()) {
//                Factory.get().getMove().moveSiteCreateOrEditActivity(requireActivity(), moveResultLauncher, null)
//            }
//            else{
//                Factory.get().getMove().moveSignInActivity(requireActivity(), updateResultLauncher)
//            }
//        }

        viewModel.user.observe(requireActivity()){

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 상태바 색상을 원래대로 복원
        requireActivity().updateStatusBarColor(true) // 원래 색상의 리소스 ID
    }


}