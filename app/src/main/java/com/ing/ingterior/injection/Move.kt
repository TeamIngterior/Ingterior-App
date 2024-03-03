package com.ing.ingterior.injection

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.ing.ingterior.ui.site.NewBluePrintFragmentDialog

abstract class Move {
    abstract fun moveMainActivity(activity: Activity)
    abstract fun moveSimpleEstimationActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>)
    abstract fun moveNewSiteActivity(activity: Activity)
    abstract fun moveNewSiteActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>)

    abstract fun showAddCodeDialog(activity: FragmentActivity)
    abstract fun showImageDialog(activity: FragmentActivity, dismissListener: NewBluePrintFragmentDialog.DialogListener?, isCreate: Boolean)

    // fragment
    abstract fun moveResultSimpleEstimationFragment(view: View)

    abstract fun moveTestActivity(activity: Activity)
    abstract fun moveSiteActivity(activity: Activity)
}