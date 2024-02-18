package com.ing.ingterior.injection

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher

abstract class Move {
    abstract fun moveMainActivity(activity: Activity)
    abstract fun moveSimpleEstimationActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity)
    abstract fun moveSiteActivity(activity: Activity, moveResultLauncher: ActivityResultLauncher<Intent>)

    // fragment
    abstract fun moveResultSimpleEstimationFragment(view: View)

    abstract fun moveTestActivity(activity: Activity)
}