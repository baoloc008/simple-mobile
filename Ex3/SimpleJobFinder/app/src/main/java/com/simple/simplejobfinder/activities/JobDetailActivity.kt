package com.simple.simplejobfinder.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.simple.simplejobfinder.R

class JobDetailActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.let {
            if (it.hasExtra(Intent.EXTRA_TEXT))
            {
//                tvJobDetailId.text = it.getStringExtra(Intent.EXTRA_TEXT)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId)
        {
            android.R.id.home ->
            {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
