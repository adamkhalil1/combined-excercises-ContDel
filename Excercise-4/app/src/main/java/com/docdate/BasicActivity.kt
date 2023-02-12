package com.docdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
    }

    fun showCustomSnackBar(message: String, errorMessage: Boolean){
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

        if(errorMessage)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.color_snackbar_error))
        else
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.color_snackbar_success))
        snackbar.show()
    }

}