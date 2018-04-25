package com.simple.youtuberemote.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;


public class VideoListAdapter extends RecyclerArrayAdapter<VideoItem>
{

  public VideoListAdapter(Context context)
  {
    super(context);
  }

  @Override
  public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
  {
    return new VideoViewHolder(parent);
  }

  public class VideoViewHolder extends BaseViewHolder<VideoItem>
  {

    ImageView mThumbnail;
    TextView  mTitle;
    TextView  mStatistics;
    TextView  mDuration;

    public VideoViewHolder(ViewGroup parent)
    {
      super(parent, R.layout.video_item);

      mThumbnail = $(R.id.iv_video_thumbnail);
      mTitle = $(R.id.tv_video_title);
      mStatistics = $(R.id.tv_video_statistics);
      mDuration = $(R.id.tv_video_duration);
    }

    @Override
    public void setData(VideoItem video)
    {
      Glide.with(getContext()).load(video.getThumbnailUrl()).into(mThumbnail);
      mTitle.setText(video.getTitle());
      mStatistics.setText(video.getChannelTitle());
      mDuration.setText(video.getDuration());
    }
  }

}
