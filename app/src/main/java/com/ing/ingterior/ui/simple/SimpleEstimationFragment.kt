package com.ing.ingterior.ui.simple

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.StaticValues.displayPixelSize
import com.ing.ingterior.util.StaticValues.getHorizontalPaddingDisplayWidth
import com.ing.ui.button.VisualButton
import com.ing.ui.text.edit.InputTextLayout


class SimpleEstimationFragment : Fragment() {

    companion object {
        private const val TAG = "InputSimpleEstimationFragment"
    }

    private val viewModel : SimpleEstimationViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SimpleEstimationViewModel::class.java] }

    private lateinit var vbButton: VisualButton
    private lateinit var ivMeterInfo: ImageView
    private lateinit var itvMeterInput: InputTextLayout
    private lateinit var lineMeter: LinearLayout
    private lateinit var lineWindow: LinearLayout
    private lateinit var lineBalcony: LinearLayout
    private lateinit var lineBathroom: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_estimation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? SimpleEstimationActivity)?.setToolbarTitle("인테리어 간편견적")
        lineMeter = view.findViewById(R.id.line_se_meter)
        vbButton = view.findViewById(R.id.vb_se_result)
        ivMeterInfo = view.findViewById(R.id.iv_se_meter_info)
        ivMeterInfo.setOnClickListener {
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.layout_meter_info_popup, null)

            val popupWidth = getHorizontalPaddingDisplayWidth(requireActivity())
            val popupWindow = PopupWindow(popupView, popupWidth, FrameLayout.LayoutParams.WRAP_CONTENT)

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true

            val closeButton = popupView.findViewById<ImageView>(R.id.iv_se_meter_info_close)
            closeButton.setOnClickListener { popupWindow.dismiss() }
            popupWindow.showAsDropDown(ivMeterInfo, 10, 0, Gravity.END)
        }

        itvMeterInput = view.findViewById(R.id.itv_se_meter_input)
        itvMeterInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                vbButton.isEnabled = !s.isNullOrEmpty()
                if(s.isNullOrEmpty()) return
                viewModel.meter = s.toString().toInt()
            }

        })


        lineWindow = view.findViewById(R.id.line_se_window_content)
        for(index in 0 until lineWindow.childCount){
            val tvWindow = lineWindow.getChildAt(index) as TextView
            tvWindow.setOnClickListener {
                if(index == 0 && viewModel.isWindow) return@setOnClickListener
                if(index == 1 && !viewModel.isWindow) return@setOnClickListener
                viewModel.isWindow = index == 0
                val tvWindowYes = lineWindow.getChildAt(0) as TextView
                val tvWindowNo = lineWindow.getChildAt(1) as TextView
                if(viewModel.isWindow) {
                    viewModel.setSelectedTextView(tvWindowYes)
                    viewModel.setDefaultTextView(tvWindowNo)
                }
                else {
                    viewModel.setDefaultTextView(tvWindowYes)
                    viewModel.setSelectedTextView(tvWindowNo)
                }
            }
        }

        lineBalcony = view.findViewById(R.id.line_se_balcony_content)
        for(index in 0 until lineBalcony.childCount){
            val tvBalcony = lineBalcony.getChildAt(index) as TextView
            tvBalcony.setOnClickListener {
                if(index == viewModel.balconyCount) return@setOnClickListener
                viewModel.balconyCount = index
                for(jIndex in 0 until lineBalcony.childCount){
                    val tvBalcony2 = lineBalcony.getChildAt(jIndex) as TextView
                    if(index == jIndex) viewModel.setSelectedTextView(tvBalcony2)
                    else viewModel.setDefaultTextView(tvBalcony2)
                }
            }
        }

        lineBathroom = view.findViewById(R.id.line_se_bathroom_content)
        for(index in 1 .. lineBathroom.childCount){
            val tvBathroom = lineBathroom.getChildAt(index-1) as TextView
            tvBathroom.setOnClickListener {
                if(index == viewModel.bathroomCount) return@setOnClickListener
                viewModel.balconyCount = index
                for(jIndex in 1 .. lineBathroom.childCount){
                    val tvBathroom2 = lineBathroom.getChildAt(jIndex-1) as TextView
                    if(index == jIndex) viewModel.setSelectedTextView(tvBathroom2)
                    else viewModel.setDefaultTextView(tvBathroom2)
                }
            }
        }

        vbButton.setOnClickListener {
            val action = SimpleEstimationFragmentDirections.actionInputSimpleEstimationFragmentToSimpleEstimationResultFragment(
                meter = viewModel.meter,
                isWindow = viewModel.isWindow,
                balconyCount = viewModel.balconyCount,
                bathroomCount = viewModel.bathroomCount
            )
            findNavController().navigate(action)
        }
    }
}