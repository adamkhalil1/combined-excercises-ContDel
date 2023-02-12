package com.docdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val btnSubmit: Button = findViewById(R.id.buttonSubmit)
        btnSubmit.setOnClickListener() {
            val etEmailId: EditText = findViewById(R.id.et_login_email)
            val emailId = etEmailId.text.toString().trim { it <= ' ' }
            if (emailId.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter Email",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailId)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Succesfully submited",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }else{
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}