package com.artech.countdown

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.content_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(toolbar)
        toolbar.setTitle("TIMER")
        toolbar.setLogo(R.mipmap.icon_timer)
        toolbar.title = "TIMER"
        toolbar.titleMarginStart = 43

        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/NotoSansCJKkr_Regular.otf")

        info_version_text.setTypeface(typefaceRegular)
        info_version_value.setTypeface(typefaceRegular)

        info_company_text.setTypeface(typefaceRegular)
        info_company_value.setTypeface(typefaceRegular)
        info_product_text.setTypeface(typefaceRegular)
        info_product_value.setTypeface(typefaceRegular)
        info_homepage_text.setTypeface(typefaceRegular)
        info_homepage_value.setTypeface(typefaceRegular)
        info_copyright_text.setTypeface(typefaceRegular)
        info_copyright_value.setTypeface(typefaceRegular)
    }
}