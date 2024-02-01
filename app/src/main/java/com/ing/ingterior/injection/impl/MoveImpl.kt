package com.ing.ingterior.injection.impl

import android.app.Activity
import android.content.Intent
import com.ing.ingterior.injection.Move
import com.ing.ingterior.ui.simple.SimpleEstimationActivity

class MoveImpl : Move() {
    override fun moveSimpleEstimationActivity(activity: Activity) {
        val intent = Intent(activity, SimpleEstimationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
        activity.finish()
    }
}