package com.ing.ingterior.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory

class HomeMainFragment : Fragment() {

    companion object {
        private const val TAG = "HomeMainFragment"
    }

    private val lineSimpleEstimateLayout:LinearLayout by lazy { requireView().findViewById(R.id.line_home_main_simple_estimation) }
    private val lineSignInLayout:LinearLayout by lazy { requireView().findViewById(R.id.line_home_main_sign_in) }
    private val lineSiteManagementLayout:LinearLayout by lazy { requireView().findViewById(R.id.line_home_main_site_management) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineSimpleEstimateLayout.setOnClickListener{
            Factory.get().getMove().moveTestActivity(requireActivity())
        }

        lineSignInLayout.setOnClickListener{
            Factory.get().getMove().moveSignInActivity(requireActivity())
        }

        lineSiteManagementLayout.setOnClickListener{
            Factory.get().getMove().moveSiteManagementActivity(requireActivity())
        }
    }


}