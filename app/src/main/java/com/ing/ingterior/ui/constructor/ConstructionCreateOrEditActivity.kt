package com.ing.ingterior.ui.constructor

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import com.ing.ingterior.EXTRA_MOVE_INDEX
import com.ing.ingterior.Logging
import com.ing.ingterior.R
import com.ing.ingterior.db.Fold
import com.ing.ingterior.db.Image.Companion.FILENAME
import com.ing.ingterior.db.Image.Companion.LOCATION
import com.ing.ingterior.db.Sign.Companion.USER_ID
import com.ing.ingterior.db.Site
import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.ImageModel
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.RoundDialogButtonListener
import com.ing.ingterior.ui.RoundDialogFragment
import com.ing.ingterior.ui.main.HomeConstructionFragment
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel
import com.ing.ingterior.util.FileWrapper
import com.ing.ingterior.util.FileWrapper.createImageFile
import com.ing.ingterior.util.FileWrapper.getFileSize
import com.ing.ingterior.util.FileWrapper.isFileUri
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.ImageUtils.getMediaFileSize
import com.ing.ingterior.util.Status
import com.ing.ui.button.VisualButton
import com.ing.ui.button.VisualDefaultButton
import com.ing.ui.check.VisualButtonCheckBox
import com.ing.ui.text.edit.InputTextLayout
import com.ing.ui.text.label.LabelView
import kotlinx.coroutines.*
import java.io.File

class ConstructionCreateOrEditActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "NewSiteActivity"
    }

    private lateinit var constructionViewModel: ConstructionViewModel

    private lateinit var dismissListener: ConstructionImageEditDialog.DialogListener
    private lateinit var getPictureResult: ActivityResultLauncher<Intent>

    private lateinit var lineProgress: LinearLayout
    private lateinit var lbTitle: LabelView
    private lateinit var ivBack: ImageView
    private lateinit var itvName: InputTextLayout
    private lateinit var btnCommit: VisualButton
    private lateinit var vcbDefects: VisualButtonCheckBox
    private lateinit var vcbManagement: VisualButtonCheckBox
    private lateinit var btnAddImage: VisualDefaultButton

    private lateinit var constAddImage: ConstraintLayout
    private lateinit var frameImageLayout: FrameLayout
    private lateinit var ivImage: ImageView
    private lateinit var btnEditImage: VisualDefaultButton
    private lateinit var btnRemoveImage: VisualDefaultButton

    private lateinit var promptRemoveImageDialog: RoundDialogFragment
    private lateinit var promptEditImageDialog: RoundDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_construction_create_or_edit)

        initBindView()

        init()

        initBindListener()

        initObserver()
    }

    private fun init() {
        constructionViewModel = ViewModelProvider(this, IngTeriorViewModelFactory())[ConstructionViewModel::class.java]

        constructionViewModel.setIntent(intent).apply {
            itvName.setText(constructionViewModel.constructionName)
            vcbDefects.setChecked(constructionViewModel.isDefects)
            vcbManagement.setChecked(constructionViewModel.isManagement)
            lbTitle.text = if(constructionViewModel.isCreate) getString(R.string.title_add_new_site) else getString(R.string.title_edit_site)
            btnCommit.setText(if(constructionViewModel.isCreate) getString(R.string.action_add_new_site) else getString(R.string.action_edit_site))
            btnCommit.isEnabled = !constructionViewModel.isCreate
        }

        dismissListener = object : ConstructionImageEditDialog.DialogListener {
            override fun onDialogDismiss() {
                lifecycleScope.launch {
                    ivImage.setImageBitmap(constructionViewModel.getImageBitmap(this@ConstructionCreateOrEditActivity))
                }
            }
        }

        getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK) {
                val photoUri = result.data?.data
                if(photoUri != null) {
                    Log.d(TAG, "getPictureResult: photoUri=$photoUri")
                    val fileSize: Long = if(isFileUri(photoUri)) getFileSize(this@ConstructionCreateOrEditActivity, photoUri)
                        else getMediaFileSize(this@ConstructionCreateOrEditActivity, photoUri)
                    Log.d(TAG, "getPictureResult: fileSize=$fileSize")
//                    if(fileSize > 5*MB){
//                        Toast.makeText(this, "이미지 파일의 크기가 너무 큽니다.", Toast.LENGTH_SHORT).show()
//                        return@registerForActivityResult
//                    }

                    var fileName: String? = FileWrapper.getImageNameFromUri(this, photoUri)

                    if(fileName?.lastIndexOf(".")!! < 0){
                        fileName = FileWrapper.createFileName(this, photoUri)
                    }

                    if(fileName!=null){
                        constructionViewModel.imageModel.postValue(ImageModel(0L, photoUri, null, fileName))
                        Factory.get().getMove().showImageDialog(this,  dismissListener, true)
                    }
                    else{
                        Toast.makeText(this, "유효 하지 않은 타입의 사진 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "사진을 불러 오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initBindView(){
        lineProgress = findViewById(R.id.line_construction_coe_progress)
        lbTitle = findViewById(R.id.lb_construction_title)
        ivBack  = findViewById(R.id.iv_construction_coe_back)
        itvName = findViewById(R.id.itv_construction_coe_name_layout)
        btnCommit = findViewById(R.id.btn_construction_coe_commit)
        vcbDefects = findViewById(R.id.vcb_construction_coe_defects)
        vcbManagement = findViewById(R.id.vcb_construction_coe_management)
        btnAddImage = findViewById(R.id.btn_construction_coe_add_image)
        constAddImage = findViewById(R.id.const_construction_coe_image)
        frameImageLayout = findViewById(R.id.frame_construction_coe_blueprint_layout)

        frameImageLayout.post {
            val params = frameImageLayout.layoutParams
            params.height = frameImageLayout.width // 너비와 같게 설정
            frameImageLayout.layoutParams = params
        }

        ivImage = findViewById(R.id.iv_site_coe_image)
        btnEditImage = findViewById(R.id.btn_site_coe_edit_image)
        btnRemoveImage = findViewById(R.id.btn_site_coe_remove_image)

        promptRemoveImageDialog = RoundDialogFragment("도면 이미지 편집/삭제 시 저장해둔 마커의 정보가 사라집니다.", "취소하기",
            "이미지 삭제하기", object : RoundDialogButtonListener{
                override fun onConfirmClicked() {
                    constructionViewModel.imageModel.postValue(null)
                }
                override fun onCancelClicked() {}
            })

        promptEditImageDialog = RoundDialogFragment("도면 이미지 편집/삭제 시 저장해둔 마커의 정보가 사라집니다.", "취소하기",
            "이미지 편집하기", object : RoundDialogButtonListener{
                override fun onConfirmClicked() {
                    Factory.get().getMove().showImageDialog(this@ConstructionCreateOrEditActivity,  dismissListener, false)
                }
                override fun onCancelClicked() {}
            })
    }

    private fun initBindListener(){
        ivBack.setOnClickListener {
            super.onBackPressed()
        }

        itvName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                constructionViewModel.constructionName = editable.toString()
                btnCommit.isEnabled = constructionViewModel.requireEnable()
            }
        })

        vcbDefects.setOnClickListener {
            vcbDefects.toggleCheck()
            constructionViewModel.isDefects = vcbDefects.isChecked()
            btnCommit.isEnabled = constructionViewModel.requireEnable()
        }

        vcbManagement.setOnClickListener {
            vcbManagement.toggleCheck()
            constructionViewModel.isManagement = vcbManagement.isChecked()
            btnCommit.isEnabled = constructionViewModel.requireEnable()
        }

        btnEditImage.setOnClickListener {
            if(constructionViewModel.isCreate) {
                Factory.get().getMove().showImageDialog(this,  dismissListener, false)
            }
            else {
                if(!promptEditImageDialog.isVisible) promptEditImageDialog.show(supportFragmentManager.beginTransaction(), "promptRemoveImageDialog")
            }
        }

        btnAddImage.setOnClickListener {
            getPictureResult.launch(ImageUtils.getPictureIntent())
        }

        btnRemoveImage.setOnClickListener {
            if(constructionViewModel.isCreate) {
                constructionViewModel.imageModel.postValue(null)
            }
            else {
                if(!promptRemoveImageDialog.isVisible) promptRemoveImageDialog.show(supportFragmentManager.beginTransaction(), "promptRemoveImageDialog")
            }
        }

        btnCommit.setOnClickListener {
            // 1. 프로그래스 바 띄움
            lineProgress.isVisible = true
            // TODO
            // 2. 서버에 SITE 관련 데이터 전송
            // 3. 서버에서 결과 받고 SITE CODE 값을 받아야 함
            // 4. SITE CODE와 같이 Local DB에 저장해야 함
            // 5. 현장 목록 화면으로 이동
            // 프로그래스 바를 먼저 보여줍니다.
            constructionViewModel.insertConstruction(this, 111, 1)

            /*CoroutineScope(Dispatchers.Main).launch {
                val context = this@ConstructionCreateOrEditActivity
                val file: File? = if (constructionViewModel.imageModel.value == null) null
                else createImageFile(context, constructionViewModel.getImageBitmap(context)!!, constructionViewModel.imageModel.value?.name ?: "")

                var operator = 0
                if (constructionViewModel.isDefects) operator += Fold.FOLD_DEFECTS
                if (constructionViewModel.isManagement) operator += Fold.FOLD_MANAGEMENT

                if(constructionViewModel.isCreate) {
                    val insertUri = Uri.parse(Site.CONTENT_URI).buildUpon().appendQueryParameter(Site.EXTRA_SITE_OPERATOR, operator.toString()).build()
                    if (withContext(Dispatchers.IO) {
                            val userId = Factory.get().getSession().getUser()?.id
                            val contentValues = ContentValues().apply {
                                put(USER_ID, userId)
                                put(Site.NAME, constructionViewModel.constructionName)
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
                }
                else{
                    // TODO 서버 연결되면 그떄 다시 생각 해보자.
//                    val updateUri = Uri.withAppendedPath(Uri.parse(Site.CONTENT_URI), siteViewModel.site?._id.toString()).buildUpon().appendQueryParameter(Site.EXTRA_SITE_OPERATOR, operator.toString()).build()
//                    if (withContext(Dispatchers.IO) {
//                            val userId = Factory.get().getSession().getUser()?.id
//                            val contentValues = ContentValues().apply {
//                                put(USER_ID, userId)
//                                put(Site.NAME, siteViewModel.siteName)
//                                put(Site.CODE, "A${System.currentTimeMillis()}")
//                                put(LOCATION, file?.absolutePath ?: "")
//                                put(FILENAME, file?.name ?: "")
//                            }
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                context.contentResolver.update(updateUri, contentValues, null) <= 0
//                            } else {
//                                context.contentResolver.update(updateUri, contentValues, "", null) <= 0
//                            }
//                        }) {
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(context, "에러 발생", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(context, "현장 수정", Toast.LENGTH_SHORT).show()
//                        }
//                    }
                }

                Toast.makeText(context, "에러 발생", Toast.LENGTH_SHORT).show()
                val intent = Intent().apply {
                    putExtra(EXTRA_MOVE_INDEX, 1)
                }
                setResult(RESULT_OK, intent)
                finish()
            }*/
        }
    }

    private fun initObserver() {
        constructionViewModel.imageModel.distinctUntilChanged().observe(this) { imageModel ->
            lifecycleScope.launch {
                if(imageModel==null) {
                    constAddImage.isVisible = false
                    btnAddImage.isVisible = true
                    ivImage.setImageURI(null)
                }
                else{
                    constAddImage.isVisible = true
                    btnAddImage.isVisible = false
                    ivImage.setImageBitmap(constructionViewModel.getImageBitmap(this@ConstructionCreateOrEditActivity))
                }
            }
        }
        constructionViewModel.insertRequestData.observe(this, Observer {
            val resource = it ?: return@Observer
            when(resource.status){
                Status.SUCCESS -> {
                    lineProgress.isVisible = false
                    finish()
                }
                Status.ERROR_INT -> {
                    lineProgress.isVisible = false
                    Toast.makeText(this, resource.intMsg!!, Toast.LENGTH_SHORT).show()
                    finish()
                }
                Status.LOADING -> {
                    lineProgress.isVisible = true
                }
                else -> return@Observer
            }

        })
    }


    fun getViewModel() = constructionViewModel

    override fun onDestroy() {
        super.onDestroy()
    }

}