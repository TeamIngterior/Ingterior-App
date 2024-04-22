package com.ing.ingterior.ui.simple

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory

class SimpleEstimationResultFragment : Fragment() {

    companion object {
        private const val TAG = "ResultSimpleEstimationFragment"
    }

    private val viewModel : SimpleEstimationViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SimpleEstimationViewModel::class.java] }
    private lateinit var lineSummary: LinearLayout
    private lateinit var lineSummaryContent: LinearLayout
    private lateinit var tvSummary: TextView
    private lateinit var tvBasicCost: TextView
    private lateinit var tvStandardCost: TextView
    private lateinit var tvAdvancedCost: TextView
    private lateinit var ivSummaryIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_estimation_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SimpleEstimationActivity)?.setToolbarTitle("인테리어 간편견적 결과")
        val args = SimpleEstimationResultFragmentArgs.fromBundle(requireArguments())
//
//        // Args 객체에서 데이터 접근
        val meter = args.meter
        val isWindow = args.isWindow
        val isWindowString = if(isWindow) "네" else "아니요"
        val balconyCount = args.balconyCount
        val bathroomCount = args.bathroomCount
        ivSummaryIcon = view.findViewById(R.id.iv_ser_summary_icon)

        tvSummary = view.findViewById(R.id.tv_ser_summary)
        tvSummary.text = "교체 면적: ${meter}㎡\n샷시 교체: ${isWindowString}\n발코니 확장: $balconyCount\n욕실 갯수: $bathroomCount"

        lineSummaryContent = view.findViewById(R.id.line_ser_summary_content)
        lineSummaryContent.isVisible = false
        lineSummary = view.findViewById(R.id.line_ser_summary)
        lineSummary.setOnClickListener {
            lineSummaryContent.isVisible = !lineSummaryContent.isVisible
            if(lineSummaryContent.isVisible) ivSummaryIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up))
            else ivSummaryIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down))
        }


        tvBasicCost = view.findViewById(R.id.tv_ser_basic_cost)
        tvStandardCost = view.findViewById(R.id.tv_ser_standard_cost)
        tvAdvancedCost = view.findViewById(R.id.tv_ser_advanced_cost)


    }

}