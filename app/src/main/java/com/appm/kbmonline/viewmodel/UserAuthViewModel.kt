package com.appm.kbmonline.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.dx.dxloadingbutton.lib.LoadingButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.doAsync


class UserAuthViewModel(application: Application) :AndroidViewModel(application), AnkoLogger {
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