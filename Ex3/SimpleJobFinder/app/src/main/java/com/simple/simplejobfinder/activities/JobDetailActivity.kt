package com.simple.simplejobfinder.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.simple.simplejobfinder.R
import kotlinx.android.synthetic.main.activity_job_detail.*

class JobDetailActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        intent?.let {
            if (it.hasExtra(Intent.EXTRA_TEXT))
            {
                tvJobDetailId.text = it.getStringExtra(Intent.EXTRA_TEXT)
            }
        }
    }

}
