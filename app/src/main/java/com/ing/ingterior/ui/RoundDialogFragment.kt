package com.ing.ingterior.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ing.ingterior.R
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.button.VisualImageButton
import com.ing.ui.text.title.TitleH4View

// 3.35.49.52 업데이트 (2023.10.20)에서 추가함
interface RoundDialogButtonListener{
    fun onConfirmClicked()
    fun onCancelClicked()
}

class RoundDialogFragment(private val title: String?, private val cancel: String?,
                          private val confirm: String?, private val buttonListener: RoundDialogButtonListener
) : DefaultDialogFragment(){

    private lateinit var vibClose: VisualImageButton
    private lateinit var th4vTitle: TitleH4View
    private lateinit var vdbCancel: VisualDefaultButton
    private lateinit var vbConfirm: VisualButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_default, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vibClose = view.findViewById(R.id.vib_default_dialog_close)
        th4vTitle = view.findViewById(R.id.tv_default_dialog_title)
        vdbCancel = view.findViewById(R.id.vdb_default_dialog_cancel)
        vbConfirm = view.findViewById(R.id.vb_default_dialog_confirm)
        th4vTitle.text = title
        vdbCancel.setText(cancel)
        vbConfirm.setText(confirm)

        vibClose.setOnClickListener{
            dismiss()
            buttonListener.onConfirmClicked()
        }

        vbConfirm.setOnClickListener {
            dismiss()
            buttonListener.onConfirmClicked()
        }
        vdbCancel.setOnClickListener{
            dismiss()
            buttonListener.onCancelClicked()
        }
    }

}