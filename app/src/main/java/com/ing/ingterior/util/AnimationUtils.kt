package com.ing.ingterior.util

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet

object AnimationUtils {

    interface AnimationListener {
        fun start()
        fun end()
    }

    fun fadeInAndOut(view: View, duration: Long, listener: AnimationListener?){
        val startAnimation = AnimationSet(true)
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = duration
        startAnimation.addAnimation(fadeInAnimation)
        val endAnimation = AnimationSet(true)
        val fadeOutAnimation = AlphaAnimation(1f, 0f)
        fadeOutAnimation.duration = duration
        endAnimation.addAnimation(fadeOutAnimation)

        startAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                listener?.start()
            }
            override fun onAnimationEnd(animation: Animation) {
                view.startAnimation(endAnimation)
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })

        view.startAnimation(startAnimation)

        endAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
                listener?.end()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
}