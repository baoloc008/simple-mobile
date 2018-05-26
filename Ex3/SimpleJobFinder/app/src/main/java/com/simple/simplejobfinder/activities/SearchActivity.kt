package com.simple.simplejobfinder.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.adapters.JobListAdapter
import com.simple.simplejobfinder.models.JobItem
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity()
{

    private val TAG = SearchActivity::class.java.simpleName

    private val _jobListAdapter = JobListAdapter(this)
    private var _isNewSearch = false;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initSearchView()
        ivBackSearch.setOnClickListener {
            back()
        }

        initJobListView()
    }

    private fun initJobListView()
    {
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this,
                                                          DividerItemDecoration.VERTICAL)
        rvSearchResults.setLayoutManager(layoutManager)
        rvSearchResults.addItemDecoration(dividerItemDecoration)
        rvSearchResults.setAdapterWithProgress(_jobListAdapter)

        _jobListAdapter.setOnItemClickListener {
            val intent = Intent(this, JobDetailActivity::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, _jobListAdapter.getItem(it).id)
            startActivity(intent)
        }

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

    private fun initSearchView()
    {
        searchView.setIconifiedByDefault(false)
        searchView.queryHint = "Tìm kiếm theo địa điểm"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
                                          {
                                              override fun onQueryTextChange(
                                                      newText: String?): Boolean
                                              {
                                                  return false
                                              }

                                              override fun onQueryTextSubmit(
                                                      query: String?): Boolean
                                              {
                                                  _isNewSearch = true

                                                  query?.let {
                                                      searchFirebase(query)
                                                  }

                                                  searchView.clearFocus()
                                                  return false
                                              }
                                          })
    }

    private fun searchFirebase(query: String = "Hồ Chí Minh")
    {
        _jobListAdapter.clear()
        _jobListAdapter.notifyDataSetChanged()

        val jobsRef = FirebaseDatabase.getInstance().getReference("job")

        jobsRef.orderByChild("location").equalTo(query)
                .addValueEventListener(object : ValueEventListener
                                       {
                                           override fun onDataChange(p0: DataSnapshot)
                                           {
                                               for (jobData in p0.children)
                                               {
                                                   Log.d(TAG, jobData.value.toString())
                                                   val job = jobData.getValue(JobItem::class.java)
                                                   job?.let {
                                                       _jobListAdapter.add(it)
                                                       _jobListAdapter.notifyDataSetChanged()
                                                   }

                                               }
                                           }

                                           override fun onCancelled(p0: DatabaseError)
                                           {
                                               Log.d(TAG, "Query failed!")
                                           }

                                       })
    }

    private fun back()
    {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
