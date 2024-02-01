package com.ing.ingterior.injection.impl

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.navigation.Navigation
import com.ing.ingterior.R
import com.ing.ingterior.injection.Move
import com.ing.ingterior.ui.sign.SignInActivity
import com.ing.ingterior.ui.simple.InputSimpleEstimationFragment
import com.ing.ingterior.ui.simple.SimpleEstimationActivity

class MoveImpl : Move() {
    override fun moveSimpleEstimationActivity(activity: Activity) {
        val intent = Intent(activity, SimpleEstimationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveSignInActivity(activity: Activity) {
        val intent = Intent(activity, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    override fun moveResultSimpleEstimationFragment(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_inputSimpleEstimationFragment_to_resultSimpleEstimationFragment)
    }
}