package com.mobile.authentification.util

import android.animation.AnimatorListenerAdapter
import android.view.View
import android.animation.Animator
import android.R.attr.toAlpha
import android.R.attr.duration
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class AppUtils {

    companion object{

        //panggil metode ini untuk membuat keyboard menghilang
         fun hideKeyboard(view: View) {
             try {
                 val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                 imm!!.hideSoftInputFromWindow(view.windowToken, 0)
             } catch (e: Exception) {
                 e.printStackTrace()
             }
         }
    }
}