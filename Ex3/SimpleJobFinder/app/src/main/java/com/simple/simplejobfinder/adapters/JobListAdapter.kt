package com.simple.simplejobfinder.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.models.JobItem

class JobListAdapter(context: Context) : RecyclerArrayAdapter<JobItem>(context)
{

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*>
    {
        return ViewHolder(parent, R.layout.job_item)
    }

    class ViewHolder(parent: ViewGroup, res: Int) :
            BaseViewHolder<JobItem>(parent, res)
    {

        private val _thumbnailView: ImageView = parent.findViewById(R.id.tvJobItemThumbnail)
        private val _titleView: TextView = parent.findViewById(R.id.tvJobItemTitle)
        private val _companyView: TextView = parent.findViewById(R.id.tvJobItemCompany)
        private val _salaryView: TextView = parent.findViewById(R.id.tvJobItemSalary)
        private val _locationView: TextView = parent.findViewById(R.id.tvJobItemLocation)

        private val _context = parent.context
        private var _jobItem: JobItem? = null

        override fun setData(jobItem: JobItem)
        {
            _jobItem = jobItem

            Glide.with(_context).load(jobItem.thumbnailUrl).asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888).into(_thumbnailView)

            _titleView.text = jobItem.title
            _companyView.text = jobItem.company
            _salaryView.text = jobItem.salary
            _locationView.text = jobItem.location
        }
    }

}
