package com.ing.ingterior.injection.impl

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.ing.ingterior.R
import com.ing.ingterior.injection.Move
import com.ing.ingterior.ui.MainActivity
import com.ing.ingterior.ui.TestActivity
import com.ing.ingterior.ui.main.SiteAddCodeDialog
import com.ing.ingterior.ui.sign.LogInActivity
import com.ing.ingterior.ui.simple.SimpleEstimationActivity
import com.ing.ingterior.ui.site.NewBluePrintFragmentDialog
import com.ing.ingterior.ui.site.NewSiteActivity
import com.ing.ingterior.ui.site.SiteActivity

class MoveImpl : Move() {
    override fun moveMainActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSimpleEstimationActivity(activity: Activity) {
        val intent = Intent(activity, SimpleEstimationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSignInActivity(activity: Activity) {
        val intent = Intent(activity, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSignInActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(activity, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        moveResultLauncher.launch(intent)
    }

    override fun moveNewSiteActivity(activity: Activity) {
        val intent = Intent(activity, NewSiteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveNewSiteActivity(
        activity: Activity,
        moveResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(activity, NewSiteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        moveResultLauncher.launch(intent)
    }

    override fun showAddCodeDialog(activity: FragmentActivity) {
        val siteAddCodeDialog = SiteAddCodeDialog()
        siteAddCodeDialog.show(activity.supportFragmentManager.beginTransaction(), "site_add_code_dialog")
    }

    override fun showImageDialog(activity: FragmentActivity, dismissListener: NewBluePrintFragmentDialog.DialogListener?, isCreate: Boolean) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        val bluePrintFragmentDialog = NewBluePrintFragmentDialog()
        bluePrintFragmentDialog.listener = dismissListener
        bluePrintFragmentDialog.isCreate = isCreate
        bluePrintFragmentDialog.show(transaction, "bluePrintFragmentDialog")
    }

    override fun moveResultSimpleEstimationFragment(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_inputSimpleEstimationFragment_to_resultSimpleEstimationFragment)
    }

    override fun moveTestActivity(activity: Activity) {
        val intent = Intent(activity, TestActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)

    }

    override fun moveSiteActivity(activity: Activity) {
        val intent = Intent(activity, SiteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)

    }
}