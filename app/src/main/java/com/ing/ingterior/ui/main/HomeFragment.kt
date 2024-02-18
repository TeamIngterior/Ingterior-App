package com.ing.ingterior.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.EXTRA_MOVE_INDEX
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainActivity
import com.ing.ingterior.ui.MainViewModel
import com.ing.ui.button.VisualButton

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private val btnSimpleEstimateLayout:VisualButton by lazy { requireView().findViewById(R.id.line_home_main_simple_estimation) }
    private val lineSiteManagement:LinearLayout by lazy { requireView().findViewById(R.id.line_home_site_management) }
    private val tvSiteManagement:TextView by lazy { requireView().findViewById(R.id.tv_home_site_management) }

    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            if(result.data == null) return@registerForActivityResult
            val index = result.data!!.getIntExtra(EXTRA_MOVE_INDEX, viewModel.currentPageIndex)
            (requireActivity() as MainActivity).selectBottomNavigationMenuItem(index)
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

        if(viewModel.isLogin()) {
            tvSiteManagement.setText(R.string.prompt_register_new_site)

            // TODO 유저의 현장이 있는지 확인하고 있으면 리스트를 보여줘야 함
        }

        btnSimpleEstimateLayout.setOnClickListener{
            Factory.get().getMove().moveSimpleEstimationActivity(requireActivity())
        }

        lineSiteManagement.setOnClickListener{
            if(Factory.get().getSession().isLogin()) {
                Factory.get().getMove().moveSiteActivity(requireActivity(), moveResultLauncher)
            }
            else{
                Factory.get().getMove().moveSignInActivity(requireActivity())
            }
        }

    }


}