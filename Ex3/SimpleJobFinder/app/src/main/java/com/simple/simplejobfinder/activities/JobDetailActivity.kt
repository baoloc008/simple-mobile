package com.simple.simplejobfinder.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.models.JobDetail
import kotlinx.android.synthetic.main.activity_job_detail.*

class JobDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.let {
            if (it.hasExtra(Intent.EXTRA_TEXT)) {
                val id = it.getStringExtra(Intent.EXTRA_TEXT)
                val jobDetailsRef = FirebaseDatabase.getInstance().getReference("job-detail")
                jobDetailsRef.orderByChild("id").equalTo(id).limitToFirst(1).addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val jobdetail = p0.getValue(JobDetail::class.java)
                        jobdetail?.let {
                            tvJobDetailBenefits.text = it.benefits
                            tvJobDetailCompany.text = it.company
                            tvJobDetailDescription.text = it.description
                            tvJobDetailExp.text = it.experience
                            tvJobDetailExpired.text = it.expired
                            tvJobDetailIntroduce.text = it.introduce
                            tvJobDetailLocation.text = it.location
                            tvJobDetailMajor.text = it.majors
                            tvJobDetailOtherInfo.text = it.otherInfo
                            tvJobDetailPosition.text = it.position
                            tvJobDetailRequirements.text = it.requirements
                            tvJobDetailSalary.text = it.salary
                            tvJobDetailTitle.text = it.jobTitle

                            Glide.with(baseContext).load(it.thumbnail).asBitmap()
                                    .format(DecodeFormat.PREFER_ARGB_8888).into(imgViewJobDetail)
                        }
                    }

                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
