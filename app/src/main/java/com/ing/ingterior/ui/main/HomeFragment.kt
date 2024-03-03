package com.ing.ingterior.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ing.ui.button.VisualDotLineButton

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private lateinit var btnSimpleEstimateLayout:VisualButton
    private  lateinit var vdlbNewSiteAction :VisualDotLineButton

    private val moveResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            if(result.data == null) return@registerForActivityResult
            val index = result.data!!.getIntExtra(EXTRA_MOVE_INDEX, viewModel.currentPageIndex)
            viewModel.getAllSiteList(requireContext(), true)
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

        btnSimpleEstimateLayout = requireView().findViewById(R.id.line_home_main_simple_estimation)
        vdlbNewSiteAction = requireView().findViewById(R.id.vdlb_home_new_site_action)



        btnSimpleEstimateLayout.setOnClickListener{
            Factory.get().getMove().moveSimpleEstimationActivity(requireActivity())
        }

        vdlbNewSiteAction.setOnClickListener{
            if(Factory.get().getSession().isLogin()) {
                Factory.get().getMove().moveNewSiteActivity(requireActivity(), moveResultLauncher)
            }
            else{
                Factory.get().getMove().moveSignInActivity(requireActivity(), updateResultLauncher)
            }
        }

        viewModel.user.observe(requireActivity()){
            if(viewModel.isLogin()) {
                vdlbNewSiteAction.setText(R.string.prompt_register_new_site)
                // TODO 유저의 현장이 있는지 확인하고 있으면 리스트를 보여줘야 함
            }
        }
    }


}