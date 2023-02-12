package com.docdate

import ProfileFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout
    private lateinit var bar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        bar = findViewById(R.id.toolbar)

        setSupportActionBar(bar)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        var user = User()
        if(intent.hasExtra("user_info")) {
            user  = intent.getParcelableExtra("user_info")!!
        }

        adapter.addFragment(DoctorsFragment(), getString(R.string.docs))
        adapter.addFragment(ProfileFragment(user), getString(R.string.profile))

        pager.adapter = adapter

        tab.setupWithViewPager(pager)


    }
}