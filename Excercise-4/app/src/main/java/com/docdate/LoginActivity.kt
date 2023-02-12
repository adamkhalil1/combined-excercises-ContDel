package com.docdate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister : TextView = findViewById(R.id.tv_login_register)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, PopUpReg::class.java))
        }

        val tvForgotPassword: TextView = findViewById(R.id.tv_login_forgotpassword)
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        val btnLogin : Button = findViewById(R.id.btn_login_login)
        btnLogin.setOnClickListener {
            logInUser()
        }

    }

    private fun logInUser(){
        val etEmailId: EditText = findViewById(R.id.et_login_email)
        val etPassword: EditText = findViewById(R.id.et_login_password)

        val emailId = etEmailId.text.toString().trim { it <= ' ' }
        val password = etPassword.text.toString().trim { it <= ' ' }

        if(validateLoginInformation(emailId, password)){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        showCustomSnackBar("you are logged in :) ", false)

                        //If user information can be retrieved from CloudFireStore
                        // => call HomeActivity (see Function userLoggedInSuccess)
                       CloudFirestore().getUserDetails(this)
                    } else {
                        showCustomSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    private fun validateLoginInformation(emailId:String, password:String): Boolean {
        val returnVal = when {
            TextUtils.isEmpty(emailId) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }  //showErrorSnackBar("All entered user details are valid.", false)
        }

        return returnVal
    }

   fun userLoggedInSuccess(user : User){
        val intent = Intent(this, HomeActivity::class.java)
        //TODO: user_info can be added to constants
        intent.putExtra("user_info", user)
        startActivity(intent)
    }
}