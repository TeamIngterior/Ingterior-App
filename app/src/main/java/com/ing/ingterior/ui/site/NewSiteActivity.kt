package com.ing.ingterior.ui.site

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.ing.ingterior.EXTRA_MOVE_INDEX
import com.ing.ingterior.R
import com.ing.ingterior.db.Fold
import com.ing.ingterior.db.Image.Companion.FILENAME
import com.ing.ingterior.db.Image.Companion.LOCATION
import com.ing.ingterior.db.Sign.Companion.USER_ID
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.BluePrintModel
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.FileWrapper
import com.ing.ingterior.util.FileWrapper.MB
import com.ing.ingterior.util.FileWrapper.createImageFile
import com.ing.ingterior.util.FileWrapper.getFileSizeFromUri
import com.ing.ingterior.util.ImageUtils
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.check.VisualButtonCheckBox
import com.ing.ui.text.edit.InputTextLayout
import kotlinx.coroutines.*
import java.io.File

class NewSiteActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "NewSiteActivity"
    }

    private val lineProgress: LinearLayout by lazy { findViewById(R.id.line_new_site_progress) }
    private val ivBack: ImageView by lazy { findViewById(R.id.iv_site_back) }
    private val itvName:InputTextLayout by lazy { findViewById(R.id.itv_new_site_name_layout) }
    private val btnAdd: VisualButton by lazy { findViewById(R.id.btn_new_site_add) }
    private val vcbDefects: VisualButtonCheckBox by lazy { findViewById(R.id.vcb_new_site_defects) }
    private val vcbManagement: VisualButtonCheckBox by lazy { findViewById(R.id.vcb_new_site_management) }
    private val btnAddImage: VisualDefaultButton by lazy { findViewById(R.id.btn_new_site_add_image) }

    private val lineAddImage: LinearLayout by lazy { findViewById(R.id.line_new_site_image) }
    private val ivImage: ImageView by lazy { findViewById(R.id.iv_new_site_image) }
    private val btnEditImage: VisualDefaultButton by lazy { findViewById(R.id.btn_new_site_edit_image) }
    private val btnRemoveImage: VisualDefaultButton by lazy { findViewById(R.id.btn_new_site_remove_image) }

    private val dismissListener = object : NewBluePrintFragmentDialog.DialogListener {
        override fun onDialogDismiss() {
            lifecycleScope.launch {
                ivImage.setImageBitmap(viewModel.getImageBitmap(this@NewSiteActivity))
            }
        }

    }

    private val viewModel : SiteViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SiteViewModel::class.java] }

    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
                var fileName: String? = FileWrapper.getImageNameFromUri(this, photoUri)

                if(fileName?.lastIndexOf(".")!! < 0){
                    fileName = FileWrapper.createFileName(this, photoUri)
                }

                val fileSize = getFileSizeFromUri(this@NewSiteActivity, photoUri)
                Log.d(TAG, "getPictureResult: fileSize=$fileSize")
                if(fileSize > 5*MB){
                    Toast.makeText(this, "이미지 파일의 크기가 너무 큽니다.", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }

                if(fileName!=null){
                    viewModel.bluePrintModel.postValue(BluePrintModel(0L, photoUri, fileName))
                    Factory.get().getMove().showImageDialog(this,  dismissListener, true)
                }
                else{
                    Toast.makeText(this, "유효하지 않은 타입의 사진입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_site)

        ivBack.setOnClickListener {
            super.onBackPressed()
        }

        itvName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                viewModel.siteName = editable.toString()
                btnAdd.isEnabled = viewModel.requireEnable()
            }

        })

        vcbDefects.setOnClickListener {
            vcbDefects.toggleCheck()
            viewModel.isDefects = vcbDefects.isChecked()
            btnAdd.isEnabled = viewModel.requireEnable()
        }

        vcbManagement.setOnClickListener {
            vcbManagement.toggleCheck()
            viewModel.isManagement = vcbManagement.isChecked()
            btnAdd.isEnabled = viewModel.requireEnable()
        }

        btnEditImage.setOnClickListener {
            Factory.get().getMove().showImageDialog(this,  dismissListener, false)
        }

        btnAddImage.setOnClickListener {
            getPictureResult.launch(ImageUtils.getPictureIntent())
        }

        btnAdd.setOnClickListener {
            // TODO
            // 1. 프로그래스 바 띄움
            // 2. 서버에 SITE 관련 데이터 전송
            // 3. 서버에서 결과 받고 SITE CODE 값을 받아야 함
            // 4. SITE CODE와 같이 Local DB에 저장해야 함
            // 5. 현장 목록 화면으로 이동
            // 프로그래스 바를 먼저 보여줍니다.
            lineProgress.isVisible = true

            CoroutineScope(Dispatchers.Main).launch {
                val context = this@NewSiteActivity
                Log.d(TAG, "onCreate: bluePrintModel=${viewModel.bluePrintModel.value}")

                val file: File? = if (viewModel.bluePrintModel.value == null) null
                else createImageFile(context, viewModel.getImageBitmap(context)!!, viewModel.bluePrintModel.value?.name ?: "")

                var operator = 0
                if (viewModel.isDefects) operator += Fold.FOLD_DEFAULT
                if (viewModel.isManagement) operator += Fold.FOLD_MANAGEMENT
                val insertUri = Uri.parse(Site.CONTENT_URI).buildUpon().appendQueryParameter(Site.EXTRA_SITE_OPERATOR, operator.toString()).build()

                if (withContext(Dispatchers.IO) {
                        val userId = Factory.get().getSession().getUser()?.id
                        val contentValues = ContentValues().apply {
                            put(USER_ID, userId)
                            put(Site.NAME, viewModel.siteName)
                            put(Site.CODE, "A${System.currentTimeMillis()}")
                            put(LOCATION, file?.absolutePath ?: "")
                            put(FILENAME, file?.name ?: "")
                        }
                        context.contentResolver.insert(insertUri, contentValues) == null
                    }) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "에러 발생", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "현장 생성", Toast.LENGTH_SHORT).show()
                    }
                }
                val intent = Intent().apply {
                    putExtra(EXTRA_MOVE_INDEX, 1)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        viewModel.bluePrintModel.distinctUntilChanged().observe(this) { bluePrint ->
            lifecycleScope.launch {
                if(bluePrint==null) {
                    lineAddImage.isVisible = false
                    btnAddImage.isVisible = true
                    ivImage.setImageURI(null)
                }
                else{
                    lineAddImage.isVisible = true
                    btnAddImage.isVisible = false
                    ivImage.setImageBitmap(viewModel.getImageBitmap(this@NewSiteActivity))
                }
            }
        }

        btnRemoveImage.setOnClickListener {
            viewModel.bluePrintModel.postValue(null)
        }
    }

    override fun onDestroy() {
        IngTeriorViewModelFactory.siteViewModel = null
        super.onDestroy()
    }

}