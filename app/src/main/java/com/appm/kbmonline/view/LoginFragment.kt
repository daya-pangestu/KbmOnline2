package com.appm.kbmonline.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.AnkoLogger
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.appm.kbmonline.R
import com.appm.kbmonline.viewmodel.UserAuthViewModel
import org.jetbrains.anko.support.v4.toast
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation
import com.dx.dxloadingbutton.lib.AnimationType


class LoginFragment : Fragment(), AnkoLogger {
    lateinit var viewModel: UserAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        viewModel = ViewModelProviders.of(this).get(UserAuthViewModel::class.java)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fLoginBtnLogin.setOnClickListener { view1 ->
            val email = fLoginEdtUser.text.toString()
            val pass = fLoginEdtPassword.text.toString()

            if (email.isNotEmpty() || pass.isNotEmpty()) {
                fLoginBtnLogin.startLoading()
                viewModel.login(email, pass) {
                    if (it.isSuccessful) {
                        fLoginBtnLogin.loadingSuccessful()
                        fLoginBtnLogin.animationEndAction = {animType ->
                            if (animType== AnimationType.SUCCESSFUL) {
                                toast("Login Succes")
                            }
                        }
                        Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signUpFragment)

                    } else {
                        fLoginBtnLogin.loadingFailed()
                        toast("${it.exception?.message}")
                    }
                }

            } else {
                Toast.makeText(view.context, "One of the field is empty", Toast.LENGTH_SHORT).show()
            }
            try {
                val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fLoginToSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signUpFragment))
    }


}
