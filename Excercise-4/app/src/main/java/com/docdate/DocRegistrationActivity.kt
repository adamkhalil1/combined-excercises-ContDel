package com.docdate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.*

class DocRegistrationActivity : BasicActivity() {
    var result = StringBuilder()
    private val PICK_IMAGE_REQUEST = 1
    private var mProgressBar: ProgressBar? = null
    private var mImageView: ImageView? = null
    private var mImageUri: Uri? = null

    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_registration)
        mProgressBar = findViewById(R.id.progress_bar);
        mImageView = findViewById(R.id.image_view);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads")

        val mButtonChooseImage: Button = findViewById(R.id.btn_upload)
        mButtonChooseImage.setOnClickListener(View.OnClickListener { view ->
            openFileChooser()
        })


        val tvLogin: TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener {
            onBackPressed()
        }

        setupActionBar()

        val btnRegister: Button = findViewById(R.id.btn_register_register)
        btnRegister.setOnClickListener {
            uploadFile()


        }
    }

    private fun setupActionBar() {
        val toolbarRegisterActivity: Toolbar =
            findViewById(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }

    //TODO put this into an interface/abstract class
    private fun validateUserInformation(): Boolean {

        val etTitle: EditText = findViewById(R.id.et_title)
        val etFirstName: EditText = findViewById(R.id.et_register_firstname)
        val etLastName: EditText = findViewById(R.id.et_register_lastname)
        val etAddress: EditText = findViewById(R.id.et_register_adress)
        val etPhone: EditText = findViewById(R.id.et_register_phone)
        val etWebsite: EditText = findViewById(R.id.et_register_website)
        val etSpecialisation: EditText = findViewById(R.id.et_register_specialization)
        val cbSvs: CheckBox = findViewById(R.id.checkBox_svs)
        val cbÖgk: CheckBox = findViewById(R.id.checkBox_ögk)
        val cbBvaeb: CheckBox = findViewById(R.id.checkBox_bvaeb)
        val cbKVA: CheckBox = findViewById(R.id.checkBox_kva)

        val etEmailId: EditText = findViewById(R.id.et_register_email)
        val etPassword: EditText = findViewById(R.id.et_register_password)
        val etConfirmPassword: EditText = findViewById(R.id.et_register_confirmpassword)
        val cbTermsAndCondition: CheckBox = findViewById(R.id.cb_register_termsandcondition)

        if (cbSvs.isChecked) {
            result.append("\nSVS")
        }
        if (cbÖgk.isChecked) {
            result.append("\nÖGK")
        }
        if (cbBvaeb.isChecked) {
            result.append("\nBVAEB")
        }
        if (cbKVA.isChecked) {
            result.append("\nKVA")
        }


        val firstName = etFirstName.text.toString().trim { it <= ' ' }
        val lastName = etLastName.text.toString().trim { it <= ' ' }
        val address = etLastName.text.toString().trim { it <= ' ' }
        val phone = etPhone.text.toString().trim { it <= ' ' }
        val title = etTitle.text.toString().trim { it <= ' ' }
        val website = etWebsite.text.toString().trim { it <= ' ' }
        val specialisation = etSpecialisation.text.toString().trim { it <= ' ' }
        val emailId = etEmailId.text.toString().trim { it <= ' ' }
        val password = etPassword.text.toString().trim { it <= ' ' }
        val confirmPassword = etConfirmPassword.text.toString().trim { it <= ' ' }
        val insurance = result.toString().trim { it <= ' ' }


        val returnVal = when {

            TextUtils.isEmpty(firstName) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(lastName) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(address) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(title) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(phone) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(specialisation) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(insurance) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(emailId) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(insurance) -> {
                showCustomSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                showCustomSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            password != confirmPassword -> {
                showCustomSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }

            !cbTermsAndCondition.isChecked -> {
                showCustomSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }

            else -> {
                true
            }  //showErrorSnackBar("All entered user details are valid.", false)
        }

        if (!returnVal) {
            result.setLength(0)
        }
        return returnVal
    }

    private fun registerNewUser(stringUri: Uri) {
        if (validateUserInformation()) {
            val title = (findViewById(R.id.et_title) as EditText).text.toString()
                .trim { it <= ' ' }
            val firstname = (findViewById(R.id.et_register_firstname) as EditText).text.toString()
                .trim { it <= ' ' }
            val lastname = (findViewById(R.id.et_register_lastname) as EditText).text.toString()
                .trim { it <= ' ' }
            val address = (findViewById(R.id.et_register_adress) as EditText).text.toString()
                .trim { it <= ' ' }
            val phone = (findViewById(R.id.et_register_phone) as EditText).text.toString()
                .trim { it <= ' ' }
            val website = (findViewById(R.id.et_register_website) as EditText).text.toString()
                .trim { it <= ' ' }
            val specialisation =
                (findViewById(R.id.et_register_specialization) as EditText).text.toString()
                    .trim { it <= ' ' }
            val insurance = result.toString().trim { it <= ' ' }
            val email: String = (findViewById(R.id.et_register_email) as EditText).text.toString()
                .trim { it <= ' ' }
            val password: String =
                (findViewById(R.id.et_register_password) as EditText).text.toString()
                    .trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!

                        Log.i(
                            "FIREBASE AUTHENTICATION",
                            "A new user is created with Firebase-User-ID: ${firebaseUser.uid}"
                        )
                        showCustomSnackBar(
                            "A new user is created with Firebase-User-ID: ${firebaseUser.uid}",
                            false
                        )
                        val user = User(
                            firebaseUser.uid,
                            "Dr",
                            title,
                            firstname,
                            lastname,
                            address,
                            phone,
                            website,
                            specialisation,
                            insurance,
                            email,
                            stringUri.toString()
                        )
                        CloudFirestore().saveUserInfoOnCloudFirestore(this, user)


                    } else {
                        showCustomSnackBar(task.exception!!.message.toString(), true)
                    }


                }
        }

    }

    fun userRegistrationSuccess() {
        Toast.makeText(this, resources.getString(R.string.register_success), Toast.LENGTH_LONG)
            .show()
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
        showCustomSnackBar(
            "openfilechoser started}",
            false
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            //Picasso.with(this).load(mImageUri).into(mImageView)
            mImageView?.setImageURI(mImageUri);
        }
    }


    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != null) {
            val fileReference: StorageReference = mStorageRef!!.child(
                System.currentTimeMillis()
                    .toString() + "." + getFileExtension(mImageUri!!)
            )
            mUploadTask = fileReference.putFile(mImageUri!!) //TODO SAfe uri in user db
                .addOnSuccessListener { taskSnapshot ->
                    var handler = Handler()
                    handler.postDelayed(Runnable { mProgressBar!!.progress = 0 }, 500)
                    Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show()

                    var uri = taskSnapshot.uploadSessionUri
                    var uri2 = taskSnapshot.metadata.toString()
                    var uri3 = mUploadTask!!.result
//                    println("----------------------------------------------------------------------done" + mUploadTask.toString() + "-------------" + uri.toString() + "uri normal = " + uri2.toString() + " MEtadatra" +uri3);

                    var stringUri = uri.toString();
                    var upload = Upload(
                        "hallo",
                        uri,
                        //TODO check that again
                    )
                    var uploadId: String = mDatabaseRef!!.push().getKey()!!;
                    mDatabaseRef!!.child(uploadId).setValue(upload)
                    fileReference.downloadUrl.addOnSuccessListener { Uri ->
                        registerNewUser(Uri)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                    override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                        val progress =
                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        mProgressBar!!.progress = progress.toInt()
                    }
                })

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

}