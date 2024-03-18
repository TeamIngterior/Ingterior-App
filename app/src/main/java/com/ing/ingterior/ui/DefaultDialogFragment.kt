package com.ing.ingterior.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.ing.ingterior.R
import com.ing.ingterior.util.DisplayUtils.dialogWidthChange
import com.ing.ingterior.util.getFloatCompat

open class DefaultDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Custom_Dialog_Default)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog != null) {
            val configuration = resources.configuration
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                dialogWidthChange(requireDialog(), resources.getFloatCompat(R.dimen.dialog_land_width_ratio))
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                dialogWidthChange(requireDialog(), resources.getFloatCompat(R.dimen.dialog_port_width_ratio))
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (dialog != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                dialogWidthChange(requireDialog(), resources.getFloatCompat(R.dimen.dialog_land_width_ratio))
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                dialogWidthChange(requireDialog(), resources.getFloatCompat(R.dimen.dialog_port_width_ratio))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}