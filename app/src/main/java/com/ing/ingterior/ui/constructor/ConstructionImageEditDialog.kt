package com.ing.ingterior.ui.constructor

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.ing.ingterior.DIALOG_LAND_WIDTH_RATIO
import com.ing.ingterior.DIALOG_PORT_WIDTH_RATIO
import com.ing.ingterior.R
import com.ing.ingterior.model.ImageModel
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel
import com.ing.ingterior.util.DisplayUtils
import com.ing.ingterior.util.DisplayUtils.getPropertyImageSize
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.label.LabelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ConstructionImageEditDialog(private val listener:DialogListener, private val isFirst: Boolean) : DialogFragment() {
    interface DialogListener {
        fun onDialogDismiss()
    }

    companion object{
        private const val TAG = "NewBluePrintFragmentDialog"
    }

    private lateinit var constContent: ConstraintLayout
    private lateinit var vibClose:VisualImageButton
    private lateinit var vibRotate:VisualImageButton
    private lateinit var vibHorizontalInversion:VisualImageButton
    private lateinit var vibVerticalInversion:VisualImageButton
    private lateinit var btnReset: VisualDefaultButton
    private lateinit var frameImageLayout: FrameLayout
    private lateinit var viewSelectImage:View
    private lateinit var labelSelectImage:LabelView
    private lateinit var ivSelectImage:ImageView
    private lateinit var btnCommit: VisualButton

    private lateinit var constructionViewModel : ConstructionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }


    private fun init(){
        setStyle(STYLE_NORMAL, R.style.Theme_Custom_Dialog_Default)
        constructionViewModel = (requireActivity() as ConstructionCreateOrEditActivity).getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_site_image_edit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBindView(view)

        initBindListener()

        constructionViewModel.imageModel.distinctUntilChanged().observe(this) { bluePrint ->
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
                    ivSelectImage.setImageBitmap(constructionViewModel.getImageBitmap(requireContext()))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val configuration = resources.configuration
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                DisplayUtils.dialogWidthChange(requireDialog(), DIALOG_LAND_WIDTH_RATIO)
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                DisplayUtils.dialogWidthChange(requireDialog(), DIALOG_PORT_WIDTH_RATIO)
            }
        }

    }


    private fun initBindView(view: View){
        constContent = view.findViewById(R.id.const_image_edit_content)
        vibClose = view.findViewById(R.id.vib_image_edit_dialog_close)
        vibRotate = view.findViewById(R.id.vib_image_edit_dialog_rotate)
        vibHorizontalInversion = view.findViewById(R.id.vib_image_edit_dialog_horizontal_inversion)
        vibVerticalInversion = view.findViewById(R.id.vib_image_edit_dialog_vertical_inversion)
        btnReset = view.findViewById(R.id.btn_image_edit_dialog_reset)
        viewSelectImage = view.findViewById(R.id.view_image_edit_dialog_select_image)
        labelSelectImage = view.findViewById(R.id.label_new_blue_print_select_image)
        ivSelectImage = view.findViewById(R.id.iv_image_edit_dialog_select_image)
        btnCommit = view.findViewById(R.id.btn_image_edit_dialog_commit)
        frameImageLayout = view.findViewById(R.id.frame_image_edit_dialog_select_image)


        val constraintSet = ConstraintSet()
        constraintSet.clone(constContent)

        constraintSet.connect(frameImageLayout.id, ConstraintSet.TOP, btnReset.id, ConstraintSet.BOTTOM, requireContext().resources.getDimensionPixelOffset(R.dimen.page_horizontal_padding))
        constraintSet.connect(frameImageLayout.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(frameImageLayout.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        val imageSize = getPropertyImageSize(requireActivity())
        constraintSet.constrainWidth(frameImageLayout.id, imageSize)
        constraintSet.constrainHeight(frameImageLayout.id, imageSize)
        constraintSet.applyTo(constContent)
    }

    private fun initBindListener() {
        vibClose.setOnClickListener {
            if(isFirst) constructionViewModel.imageModel.postValue(null)
            dismiss()
        }

        vibRotate.setOnClickListener {
            if(constructionViewModel.imageModel.value ==null) {
                return@setOnClickListener
            }
            lifecycleScope.launch {
                constructionViewModel.imageModel.value?.increaseRotation()

                ivSelectImage.setImageBitmap(constructionViewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        vibHorizontalInversion.setOnClickListener {
            if(constructionViewModel.imageModel.value ==null) {
                return@setOnClickListener
            }

            lifecycleScope.launch {
                constructionViewModel.imageModel.value?.updateHorizontalInversion()

                ivSelectImage.setImageBitmap(constructionViewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        vibVerticalInversion.setOnClickListener {
            if(constructionViewModel.imageModel.value ==null) {
                return@setOnClickListener
            }

            lifecycleScope.launch {
                constructionViewModel.imageModel.value?.updateVerticalInversion()
                ivSelectImage.setImageBitmap(constructionViewModel.reScaleBluePrintImage(requireContext()))
            }
        }

        btnReset.setOnClickListener {
            val imageModel = constructionViewModel.imageModel.value ?: return@setOnClickListener
            val ifNeedReset = imageModel.horizontalInversion || imageModel.verticalInversion || imageModel.rotation > 0
            if(ifNeedReset) {
                lifecycleScope.launch{
                    withContext(Dispatchers.IO){
                        constructionViewModel.imageModel.postValue(ImageModel(0L, imageModel.uri, null, imageModel.name))
                    }
                    ivSelectImage.setImageBitmap(constructionViewModel.reScaleBluePrintImage(requireContext()))
                }
            }
        }

        btnCommit.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDialogDismiss()
    }
}