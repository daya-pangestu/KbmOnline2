package com.appm.kbmonline.view


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.AnkoLogger
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.appm.kbmonline.R
import com.appm.kbmonline.viewmodel.UserAuthViewModel


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

        viewModel::login

        fLoginBtnLogin.setOnClickListener { view1 ->
            val email = fLoginEdtUser.text.toString()
            val pass = fLoginEdtPassword.text.toString()
            val progressDialog = ProgressDialog(context)

            progressDialog.apply {
                isIndeterminate = true
                setMessage("Authenticating...")
                show()
            }
            viewModel.login(email, pass)

            progressDialog.dismiss()
        }
    }


}
