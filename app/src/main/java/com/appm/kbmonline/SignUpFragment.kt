package com.appm.kbmonline

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.appm.kbmonline.viewmodel.UserAuthViewModel
import com.dx.dxloadingbutton.lib.AnimationType
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.jetbrains.anko.support.v4.toast

class SignUpFragment : Fragment() {
    lateinit var userAuthViewModel :UserAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_sign_up, container, false)
        userAuthViewModel = ViewModelProviders.of(this).get(UserAuthViewModel::class.java)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fsignUpToLogin.setOnClickListener{Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_loginFragment)        }

        fsignUpBtnSignUp.setOnClickListener {
            val email = fsignUpEdtEmail.text.toString()
            val pass = fsignUpEdtPass.text.toString()

            if (email.isNotEmpty() || pass.isNotEmpty()) {
                fsignUpBtnSignUp.startLoading()
                userAuthViewModel.signUp(email, pass) {
                    if (it.isSuccessful) {
                        fsignUpBtnSignUp.loadingSuccessful()
                        fsignUpBtnSignUp.animationEndAction = {animType ->
                            //TODO move to login fragment with animation when user succesfully created
                            if (animType== AnimationType.SUCCESSFUL) {
                                toast("Login Succes")
                                Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_loginFragment)
                            }
                        }
                    } else {
                        fsignUpBtnSignUp.loadingFailed()
                        toast("${it.exception?.message}")
                    }
                }

            } else {
                Toast.makeText(view.context, "One of the field is empty", Toast.LENGTH_SHORT).show()
            }
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
