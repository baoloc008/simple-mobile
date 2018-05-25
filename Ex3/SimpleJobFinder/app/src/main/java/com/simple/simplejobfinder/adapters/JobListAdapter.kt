package com.simple.simplejobfinder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.simple.simplejobfinder.R
import com.simple.simplejobfinder.models.JobItem
import kotlinx.android.synthetic.main.job_item.view.*

class JobListAdapter(context: Context) : RecyclerArrayAdapter<JobItem>(context)
{

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*>
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        println(parent)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) :
            BaseViewHolder<JobItem>(itemView)
    {

        private val _thumbnailView: ImageView
        private val _titleView: TextView
        private val _companyView: TextView
        private val _salaryView: TextView
        private val _locationView: TextView

        private val _context = itemView.context
        private var _jobItem: JobItem? = null

        init
        {
            _thumbnailView = itemView.tvJobItemThumbnail
            _titleView = itemView.tvJobItemTitle
            _companyView = itemView.tvJobItemCompany
            _salaryView = itemView.tvJobItemSalary
            _locationView = itemView.tvJobItemLocation
        }

        override fun setData(jobItem: JobItem)
        {
            _jobItem = jobItem

            Glide.with(_context).load(jobItem.thumbnail).asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888).into(_thumbnailView)

            _titleView.text = jobItem.jobTitle
            _companyView.text = jobItem.company
            _salaryView.text = jobItem.salary
            _locationView.text = jobItem.location
        }
    }

}
