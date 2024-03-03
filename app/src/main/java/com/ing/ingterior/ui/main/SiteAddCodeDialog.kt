package com.ing.ingterior.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.ing.ingterior.R
import com.ing.ingterior.util.DisplayUtils.dialogWidthChange
import com.ing.ui.button.VisualImageButton

class SiteAddCodeDialog : DialogFragment() {

    private lateinit var vibClose: VisualImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.Theme_Custom_Dialog_Default)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site_add_code_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vibClose = requireView().findViewById(R.id.vib_add_code_close)

        vibClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val configuration = resources.configuration
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                dialogWidthChange(requireDialog(), resources.getFloat(R.dimen.dialog_land_width_ratio))
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                dialogWidthChange(requireDialog(), resources.getFloat(R.dimen.dialog_port_width_ratio))
            }
        }
    }

    companion object {

    }
}