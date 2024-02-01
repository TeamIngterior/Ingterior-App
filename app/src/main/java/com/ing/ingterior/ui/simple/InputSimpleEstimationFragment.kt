package com.ing.ingterior.ui.simple

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.Move
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.start.StartViewModel

class InputSimpleEstimationFragment : Fragment() {

    companion object {
        private const val TAG = "InputSimpleEstimationFragment"
    }

    private val viewModel : SimpleEstimationViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SimpleEstimationViewModel::class.java] }
    private val btnResult: Button by lazy { requireView().findViewById(R.id.btn_ise_result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_simple_estimation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnResult.setOnClickListener {
            Factory.get().getMove().moveResultSimpleEstimationFragment(requireView())
        }
    }
}