package com.appm.kbmonline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger
import timber.log.Timber


class UserAuthViewModel(application: Application) : AndroidViewModel(application), AnkoLogger {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String,task : (Task<AuthResult>) ->Unit ) {

        Timber.i("loading...")
        viewModelScope.launch {

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task(it)
                }
        }
    }


    fun login(email: String, password: String, task: (Task<AuthResult>) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                task(it)
            }

    }

}