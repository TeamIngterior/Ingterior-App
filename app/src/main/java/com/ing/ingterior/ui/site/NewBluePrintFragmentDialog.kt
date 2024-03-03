package com.ing.ingterior.ui.site

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.ing.ingterior.R
import com.ing.ingterior.model.BluePrintModel
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.DisplayUtils
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.label.LabelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewBluePrintFragmentDialog : DialogFragment() {
    interface DialogListener {
        fun onDialogDismiss()
    }

    companion object{
        private const val TAG = "NewBluePrintFragmentDialog"
    }

    var listener: DialogListener? = null
    var isCreate = false

    private val vibClose:VisualImageButton by lazy { requireView().findViewById(R.id.vib_new_blue_print_close) }
    private val vibRotate:VisualImageButton by lazy { requireView().findViewById(R.id.vib_new_blue_print_rotate) }
    private val vibHorizontalInversion:VisualImageButton by lazy { requireView().findViewById(R.id.vib_new_blue_print_horizontal_inversion) }
    private val vibVerticalInversion:VisualImageButton by lazy { requireView().findViewById(R.id.vib_new_blue_print_vertical_inversion) }
    private val btnReset: VisualDefaultButton by lazy { requireView().findViewById(R.id.btn_blue_print_reset) }

    private val viewSelectImage:View by lazy { requireView().findViewById(R.id.view_new_blue_print_select_image) }
    private val labelSelectImage:LabelView by lazy { requireView().findViewById(R.id.label_new_blue_print_select_image) }
    private val ivSelectImage:ImageView by lazy { requireView().findViewById(R.id.iv_new_blue_print_select_image) }
    private val btnCommit: VisualButton by lazy { requireView().findViewById(R.id.btn_new_blue_print_commit) }

    private val viewModel : SiteViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.Theme_Custom_Dialog_Default)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_blue_print_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vibClose.setOnClickListener {
            if(isCreate) viewModel.bluePrintModel.postValue(null)
            dismiss()
        }

        vibRotate.setOnClickListener {
            if(viewModel.bluePrintModel.value ==null) {
                return@setOnClickListener
            }
            lifecycleScope.launch {
                viewModel.bluePrintModel.value?.increaseRotation()

                ivSelectImage.setImageBitmap(viewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        vibHorizontalInversion.setOnClickListener {
            if(viewModel.bluePrintModel.value ==null) {
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.bluePrintModel.value?.updateHorizontalInversion()

                ivSelectImage.setImageBitmap(viewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        vibVerticalInversion.setOnClickListener {
            if(viewModel.bluePrintModel.value ==null) {
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.bluePrintModel.value?.updateVerticalInversion()
                ivSelectImage.setImageBitmap(viewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        btnReset.setOnClickListener {
            val bluePrint = viewModel.bluePrintModel.value ?: return@setOnClickListener
            val ifNeedReset = bluePrint.horizontalInversion || bluePrint.verticalInversion || bluePrint.rotation > 0
            if(ifNeedReset) {
                lifecycleScope.launch{
                    withContext(Dispatchers.IO){
                        viewModel.bluePrintModel.postValue(BluePrintModel(0L, bluePrint.uri, bluePrint.name))
                    }
                    ivSelectImage.setImageBitmap(viewModel.reScaleBluePrintImage(requireContext()))
                }
            }


        }

//        viewSelectImage.setOnClickListener {
//            getPictureResult.launch(ImageUtils.getPictureIntent())
//        }

        btnCommit.setOnClickListener {
            dismiss()
        }

        viewModel.bluePrintModel.distinctUntilChanged().observe(this) { bluePrint ->
            lifecycleScope.launch {
                if(bluePrint==null) {
                    labelSelectImage.isVisible = true
                    ivSelectImage.isVisible = false
                    btnCommit.isEnabled = false
                }
                else{
                    labelSelectImage.isVisible = false
                    ivSelectImage.isVisible = true
                    btnCommit.isEnabled = true
                    ivSelectImage.setImageBitmap(viewModel.getImageBitmap(requireContext()))
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val configuration = resources.configuration
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                DisplayUtils.dialogWidthChange(requireDialog(), resources.getFloat(R.dimen.dialog_land_width_ratio))
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                DisplayUtils.dialogWidthChange(requireDialog(), resources.getFloat(R.dimen.dialog_port_width_ratio))
            }
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onDialogDismiss()
    }
}