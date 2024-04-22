package com.ing.ingterior.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.MainViewModel

class SettingFragment : Fragment() {
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }

    private lateinit var lineDonation: LinearLayout
    private lateinit var lineInquiry: LinearLayout
    private lateinit var lineWithdraw: LinearLayout
    private lateinit var lineFeedback: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.currentPageIndex = 3
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineDonation = view.findViewById(R.id.line_setting_donation)
        lineInquiry = view.findViewById(R.id.line_setting_inquiry)
        lineWithdraw = view.findViewById(R.id.line_setting_withdraw)

        lineDonation.setOnClickListener {
            Factory.get().getMove().moveDonationActivity(requireActivity())
        }

        lineInquiry.setOnClickListener {
            Factory.get().getMove().moveInquiryActivity(requireActivity())
        }

        lineWithdraw.setOnClickListener {
            Factory.get().getMove().moveWithdrawActivity(requireActivity())
        }

    }

}