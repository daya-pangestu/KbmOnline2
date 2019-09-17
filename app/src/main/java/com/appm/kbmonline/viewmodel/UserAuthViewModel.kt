package com.appm.kbmonline.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast


class UserAuthViewModel(application: Application) :AndroidViewModel(application){
    private val mJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +mJob)
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) {

        Timber.i("loading...")


        uiScope.launch(Dispatchers.Default) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.i("createUserWithEmail:success")
                        Toast.makeText(
                            getApplication(), "register succes.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w("createUserWithEmail:failure ${it.exception}")
                        Toast.makeText(
                            getApplication(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }
}