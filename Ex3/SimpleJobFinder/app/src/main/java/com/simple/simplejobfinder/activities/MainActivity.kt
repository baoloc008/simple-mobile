package com.simple.simplejobfinder.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.adapters.JobListAdapter
import com.simple.simplejobfinder.models.JobItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerArrayAdapter.OnLoadMoreListener
{

    private val _jobListAdapter = JobListAdapter(this)
    private var _offset = 0
    private val _limit = 10

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initJobListView()
        loadFromFirebase(_offset, _limit)
    }

    override fun onLoadMore()
    {
        _offset += _limit + 1
        loadFromFirebase(_offset, _limit)
    }

    private fun loadFromFirebase(offset: Int, limit: Int)
    {
        val startAt = offset.toString()
        val endAt = (offset + limit).toString()
        val mData = FirebaseDatabase.getInstance().reference
        mData.child("job").orderByKey().startAt(startAt).endAt(endAt)
                .addChildEventListener(object : ChildEventListener
                                       {
                                           override fun onCancelled(p0: DatabaseError)
                                           {
                                               _jobListAdapter.pauseMore()
                                               rvJobList.showError()
                                           }

                                           override fun onChildMoved(p0: DataSnapshot, p1: String?)
                                           {
                                           }

                                           override fun onChildChanged(p0: DataSnapshot,
                                                                       p1: String?)
                                           {
                                           }

                                           override fun onChildRemoved(p0: DataSnapshot)
                                           {
                                           }

                                           override fun onChildAdded(p0: DataSnapshot, p1: String?)
                                           {
                                               val job = p0.getValue(JobItem::class.java)
                                               job?.let {
                                                   _jobListAdapter.add(it)
                                                   _jobListAdapter.notifyDataSetChanged()
                                               }
                                           }
                                       })
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

        _jobListAdapter.setMore(R.layout.rv_job_list_more, this)
        _jobListAdapter.setNoMore(R.layout.rv_job_list_nomore)
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
//        _jobListAdapter.pauseMore()
    }
}
