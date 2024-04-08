package com.ing.ingterior.injection

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.ing.ingterior.db.Site
import com.ing.ingterior.model.EventModel
import com.ing.ingterior.ui.site.SiteImageEditDialog

abstract class Move {
    abstract fun moveMainActivity(activity: Activity)
    abstract fun moveSimpleEstimationActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>)
    abstract fun moveSiteCreateOrEditActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>?, site: Site?)

    abstract fun moveSiteEventActivity(activity: Activity, event: EventModel, site: Site)

    abstract fun showAddCodeDialog(activity: FragmentActivity)
    abstract fun showImageDialog(activity: FragmentActivity, dismissListener: SiteImageEditDialog.DialogListener, isFirst: Boolean)

    // fragment
    abstract fun moveResultSimpleEstimationFragment(view: View)

    abstract fun moveTestActivity(activity: Activity)
    abstract fun moveSiteActivity(activity: Activity, site: Site)

    abstract fun moveSiteInsertDefectActivity(activity: Activity, site: Site)

    abstract fun moveSiteInsertManagementActivity(activity: Activity, site: Site)
}