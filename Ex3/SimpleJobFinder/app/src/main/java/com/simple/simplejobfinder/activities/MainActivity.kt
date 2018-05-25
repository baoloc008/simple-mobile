package com.simple.simplejobfinder.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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

        initJobListView()

        mockJobListData()

    }

    private fun mockJobListData()
    {
        // mock data
        _jobItems.add(
                JobItem("https://careerbuilder.vn/vi/tim-viec-lam/ky-su-giam-sat-xay-dung.35AEE43B.html",
                        "Kỹ Sư Giám Sát Xây Dựng 1", "Công Ty Cổ Phần Kềm Nghĩa",
                        "15 Tr - 25 Tr VND",
                        "Hồ Chí Minh",
                        "https://images.careerbuilder.vn/employers/7837/104538logokemnghia_95pxx50px.jpg"))
        _jobItems.add(
                JobItem("https://careerbuilder.vn/vi/tim-viec-lam/ky-su-giam-sat-xay-dung.35AEE43B.html",
                        "Kỹ Sư Giám Sát Xây Dựng 2", "Công Ty Cổ Phần Kềm Nghĩa",
                        "15 Tr - 25 Tr VND",
                        "Hồ Chí Minh",
                        "https://images.careerbuilder.vn/employers/7837/104538logokemnghia_95pxx50px.jpg"))
        _jobItems.add(
                JobItem("https://careerbuilder.vn/vi/tim-viec-lam/ky-su-giam-sat-xay-dung.35AEE43B.html",
                        "Kỹ Sư Giám Sát Xây Dựng 3", "Công Ty Cổ Phần Kềm Nghĩa",
                        "15 Tr - 25 Tr VND",
                        "Hồ Chí Minh",
                        "https://images.careerbuilder.vn/employers/7837/104538logokemnghia_95pxx50px.jpg"))
        _jobItems.add(
                JobItem("https://careerbuilder.vn/vi/tim-viec-lam/ky-su-giam-sat-xay-dung.35AEE43B.html",
                        "Kỹ Sư Giám Sát Xây Dựng 4", "Công Ty Cổ Phần Kềm Nghĩa",
                        "15 Tr - 25 Tr VND",
                        "Hồ Chí Minh",
                        "https://images.careerbuilder.vn/employers/7837/104538logokemnghia_95pxx50px.jpg"))

        // add to adapter
        _jobListAdapter.addAll(_jobItems)
    }

    private fun initJobListView()
    {
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this,
                                                          DividerItemDecoration.VERTICAL)
        rvJobList.setLayoutManager(layoutManager)
        rvJobList.addItemDecoration(dividerItemDecoration)
        rvJobList.setAdapterWithProgress(_jobListAdapter)

        _jobListAdapter.setOnItemClickListener {
            val intent = Intent(this, JobDetailActivity::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, _jobListAdapter.getItem(it).id)
            startActivity(intent)
        }

//        _jobListAdapter.setMore(R.layout.rv_more, this)
//        _jobListAdapter.setError(R.layout.rv_job_list_error,
//                                 object : RecyclerArrayAdapter.OnErrorListener
//                                 {
//                                     override fun onErrorShow()
//                                     {
//                                         _jobListAdapter.resumeMore()
//                                     }
//
//                                     override fun onErrorClick()
//                                     {
//                                         _jobListAdapter.resumeMore()
//                                     }
//                                 })

        _jobListAdapter.clear()
//        _jobListAdapter.pauseMore()
    }
}
