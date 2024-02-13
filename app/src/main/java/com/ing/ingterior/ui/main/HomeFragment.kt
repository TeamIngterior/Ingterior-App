package com.ing.ingterior.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.MainViewModel

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }
    private val lineSimpleEstimateLayout:LinearLayout by lazy { requireView().findViewById(R.id.line_home_main_simple_estimation) }
    private val lineSiteManagement:LinearLayout by lazy { requireView().findViewById(R.id.line_home_site_management) }
    private val tvSiteManagement:TextView by lazy { requireView().findViewById(R.id.tv_home_site_management) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        lineSimpleEstimateLayout.setOnClickListener{
            Factory.get().getMove().moveTestActivity(requireActivity())
        }

        lineSiteManagement.setOnClickListener{
            if(Factory.get().getSession().isLogin()) {

            }
            else{
                Factory.get().getMove().moveSignInActivity(requireActivity())
            }
        }

    }


}