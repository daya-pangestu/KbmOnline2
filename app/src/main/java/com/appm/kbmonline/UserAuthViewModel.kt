package com.appm.kbmonline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import timber.log.Timber


class UserAuthViewModel(application: Application) : AndroidViewModel(application), AnkoLogger {
    private val mJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO +mJob)
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String,task : (Task<AuthResult>) ->Unit ) {

        Timber.i("loading...")
        uiScope.launch {

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task(it)
                }
        }
    }


    fun login(email: String, password: String,task : (Task<AuthResult>) ->Unit) {
        uiScope.launch {
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    task(it)
                }
        }
    }


    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }
}