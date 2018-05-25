package com.simple.simplejobfinder.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.adapters.JobListAdapter
import com.simple.simplejobfinder.models.JobItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private val _jobListAdapter = JobListAdapter(this)
    private val _jobItems = ArrayList<JobItem>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Log.d("token", FirebaseInstanceId.getInstance().token)
        val mData = FirebaseDatabase.getInstance().reference
        mData.child("location").addChildEventListener(object : ChildEventListener
                                                      {
                                                          override fun onCancelled(
                                                                  p0: DatabaseError)
                                                          {
                                                          }

                                                          override fun onChildMoved(
                                                                  p0: DataSnapshot, p1: String?)
                                                          {
                                                          }

                                                          override fun onChildChanged(
                                                                  p0: DataSnapshot, p1: String?)
                                                          {
                                                          }

                                                          override fun onChildRemoved(
                                                                  p0: DataSnapshot)
                                                          {
                                                          }

                                                          override fun onChildAdded(
                                                                  p0: DataSnapshot, p1: String?)
                                                          {
                                                              val location = p0.getValue(
                                                                      String::class.java)
                                                              Log.d("location", location)
                                                          }
                                                      })

//        initJobListView()

//        _jobItems.add(JobItem("000", "Title", "Company", "salary", "Location", ""))
//        _jobListAdapter.addAll(_jobItems)
    }

    private fun initJobListView()
    {
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this,
                                                          DividerItemDecoration.VERTICAL)
        rvJobList.setLayoutManager(layoutManager)
        rvJobList.addItemDecoration(dividerItemDecoration)
        rvJobList.setAdapterWithProgress(_jobListAdapter)

//        _jobListAdapter.setMore(R.layout.rv_more, this)
        _jobListAdapter.setError(R.layout.rv_job_list_error,
                                 object : RecyclerArrayAdapter.OnErrorListener
                                 {
                                     override fun onErrorShow()
                                     {
                                         _jobListAdapter.resumeMore()
                                     }

                                     override fun onErrorClick()
                                     {
                                         _jobListAdapter.resumeMore()
                                     }
                                 })

        _jobListAdapter.clear()
        _jobListAdapter.pauseMore()
    }
}
