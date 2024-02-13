package com.ing.ingterior.injection

import android.app.Activity
import android.view.View

abstract class Move {
    abstract fun moveMainActivity(activity: Activity)
    abstract fun moveSimpleEstimationActivity(activity: Activity)
    abstract fun moveSignInActivity(activity: Activity)
    abstract fun moveResultSimpleEstimationFragment(view: View)
    abstract fun moveTestActivity(activity: Activity)

}