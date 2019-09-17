package com.appm.kbmonline.util

import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.graphics.alpha
import android.animation.Animator
import android.R.attr.toAlpha
import android.R.attr.duration



class Utils {
    companion object{

        fun animateView(view : View, tovisibility : Int, toalpha : Float,duration : Int){
            val show = tovisibility == View.VISIBLE

            if (show){
                view.alpha = 0F
            }

            view.apply {
                visibility = View.VISIBLE
                animate()
                    .setDuration(duration.toLong())
                    .alpha(if (show) toalpha else 0F)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            view.visibility = tovisibility
                        }
                    })
            }
        }
    }
}