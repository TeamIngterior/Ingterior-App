package com.ing.ingterior.injection.impl

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.ing.ingterior.EXTRA_CONVERSATION
import com.ing.ingterior.EXTRA_EVENT
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.R
import com.ing.ingterior.db.Site
import com.ing.ingterior.injection.Move
import com.ing.ingterior.model.ConversationModel
import com.ing.ingterior.model.EventModel
import com.ing.ingterior.ui.MainActivity
import com.ing.ingterior.ui.TestActivity
import com.ing.ingterior.ui.chat.MessageListActivity
import com.ing.ingterior.ui.main.SiteAddCodeDialog
import com.ing.ingterior.ui.log.LogInActivity
import com.ing.ingterior.ui.setting.SettingDonationActivity
import com.ing.ingterior.ui.setting.SettingInquiryActivity
import com.ing.ingterior.ui.setting.SettingWithdrawActivity
import com.ing.ingterior.ui.simple.SimpleEstimationActivity
import com.ing.ingterior.ui.site.SiteImageEditDialog
import com.ing.ingterior.ui.site.SiteCreateOrEditActivity
import com.ing.ingterior.ui.site.SiteActivity
import com.ing.ingterior.ui.site.SiteEventActivity
import com.ing.ingterior.ui.site.defects.SiteInsertDefectsActivity
import com.ing.ingterior.ui.site.management.SiteCreateManagementActivity

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

    override fun moveSiteCreateOrEditActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>?, site: Site?) {
        val intent = Intent(activity, SiteCreateOrEditActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(EXTRA_SITE, site)
        if(moveResultLauncher!=null) {
            moveResultLauncher.launch(intent)
        }
        else{
            activity.startActivity(intent)
        }
    }

    override fun moveSiteEventActivity(activity: Activity, event: EventModel, site: Site) {
        val intent = Intent(activity, SiteEventActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(EXTRA_EVENT, event)
        intent.putExtra(EXTRA_SITE, site)
        activity.startActivity(intent)
    }

    override fun showAddCodeDialog(activity: FragmentActivity) {
        val siteAddCodeDialog = SiteAddCodeDialog()
        siteAddCodeDialog.show(activity.supportFragmentManager.beginTransaction(), "site_add_code_dialog")
    }

    override fun showImageDialog(activity: FragmentActivity, dismissListener: SiteImageEditDialog.DialogListener, isFirst: Boolean) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        val bluePrintFragmentDialog = SiteImageEditDialog(dismissListener, isFirst)
        bluePrintFragmentDialog.show(transaction, "bluePrintFragmentDialog")
    }

    override fun moveResultSimpleEstimationFragment(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_inputSimpleEstimationFragment_to_simpleEstimationResultFragment)
    }

    override fun moveTestActivity(activity: Activity) {
        val intent = Intent(activity, TestActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSiteActivity(activity: Activity, site: Site) {
        val intent = Intent(activity, SiteActivity::class.java)
        intent.putExtra(EXTRA_SITE, site)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)

    }

    override fun moveSiteInsertDefectActivity(activity: Activity, site: Site) {
        val intent = Intent(activity, SiteInsertDefectsActivity::class.java)
        intent.putExtra(EXTRA_SITE, site)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSiteInsertManagementActivity(activity: Activity, site: Site) {
        val intent = Intent(activity, SiteCreateManagementActivity::class.java)
        intent.putExtra(EXTRA_SITE, site)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveMessageListActivity(activity: Activity, conversationModel: ConversationModel) {
        val intent = Intent(activity, MessageListActivity::class.java)
        intent.putExtra(EXTRA_CONVERSATION, conversationModel)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveInquiryActivity(activity: Activity) {
        val intent = Intent(activity, SettingInquiryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveDonationActivity(activity: Activity) {
        val intent = Intent(activity, SettingDonationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveWithdrawActivity(activity: Activity) {
        val intent = Intent(activity, SettingWithdrawActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }
}