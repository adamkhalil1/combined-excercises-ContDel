package com.docdate

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class PopUpReg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_reg);

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels
        window.setLayout(width, height)
        window.setLayout((width * 1).toInt(), (height * 0.5).toInt())

        val btnDoc: Button = findViewById(R.id.btn_doctor)
        val btnClient: Button = findViewById(R.id.btn_client)
        btnDoc.setOnClickListener {
            startActivity(Intent(this, DocRegistrationActivity::class.java))
        }
        btnClient.setOnClickListener {
            startActivity(Intent(this, ClientRegistrationActivity::class.java))
        }

    }
}