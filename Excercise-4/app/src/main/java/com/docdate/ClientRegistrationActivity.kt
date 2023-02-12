package com.docdate

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class ClientRegistrationActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_registration)

        val tvLogin: TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener {
            //ATTENTION: ATTENTION: We do not need to call a new intent here, because we can only
            // get to this activity from the LoginActivity. Therefore the function onBackPressed()
            // is more than enough!
            /*val intent : Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)*/
            onBackPressed()
        }

        setupActionBar()

        val btnRegister : Button = findViewById(R.id.btn_register_register)
        btnRegister.setOnClickListener {
            registerNewUser()
        }
    }

    private fun setupActionBar(){
        val toolbarRegisterActivity : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegisterActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun validateUserInformation(): Boolean {
        val etFirstName: EditText = findViewById(R.id.et_register_firstname)
        val etLastName: EditText = findViewById(R.id.et_register_lastname)
        val etEmailId: EditText = findViewById(R.id.et_register_email)
        val etPassword: EditText = findViewById(R.id.et_register_password)
        val etConfirmPassword: EditText = findViewById(R.id.et_register_confirmpassword)
        val cbTermsAndCondition: CheckBox = findViewById(R.id.cb_register_termsandcondition)

        val firstName = etFirstName.text.toString().trim { it <= ' ' }
        val lastName = etLastName.text.toString().trim { it <= ' ' }
        val emailId = etEmailId.text.toString().trim { it <= ' ' }
        val password = etPassword.text.toString().trim { it <= ' ' }
        val confirmPassword = etConfirmPassword.text.toString().trim { it <= ' ' }


        val returnVal = when {

            TextUtils.isEmpty(firstName) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(lastName) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(emailId) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(confirmPassword) ->{
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            password != confirmPassword -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            !cbTermsAndCondition.isChecked -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }

            else -> {
                true
            }  //showErrorSnackBar("All entered user details are valid.", false)
        }

        return returnVal
    }

    private fun registerNewUser() {
        if(validateUserInformation()){

            val firstname = (findViewById(R.id.et_register_firstname) as EditText).text.toString().trim { it <= ' ' }
            val lastname = (findViewById(R.id.et_register_lastname) as EditText).text.toString().trim { it <= ' ' }
            val email: String = (findViewById(R.id.et_register_email) as EditText).text.toString().trim { it <= ' ' }
            val password: String = (findViewById(R.id.et_register_password) as EditText).text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val firebaseUser = task.result!!.user!!

                        Log.i("FIREBASE AUTHENTICATION", "A new user is created with Firebase-User-ID: ${firebaseUser.uid}")
                        showCustomSnackBar("A new user is created with Firebase-User-ID: ${firebaseUser.uid}", false)

                        val user = User(firebaseUser.uid, "client", "", firstname,lastname,"","","","","",email)
                        CloudFirestore().saveClientUserInfoOnCloudFirestore(this, user)
                    } else {
                        showCustomSnackBar(task.exception!!.message.toString(), true)
                    }

                }
        }

    }

    fun userRegistrationSuccess() {
        Toast.makeText(this, resources.getString(R.string.register_success), Toast.LENGTH_LONG).show()
    }
}