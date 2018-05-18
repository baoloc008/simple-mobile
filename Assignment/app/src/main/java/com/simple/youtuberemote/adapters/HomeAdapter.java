package com.simple.youtuberemote.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by loc on 15/04/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{
  private Context                                context;
  private List<VideoItem>                        videoList;
  private RecyclerViewHomeAdapterOnclickListener mRecyclerViewHomeAdapterOnclickListener;

  public HomeAdapter(Context context,
                     List<VideoItem> videoList,
                     RecyclerViewHomeAdapterOnclickListener listener)
  {
    this.context = context;
    this.videoList = videoList;
    this.mRecyclerViewHomeAdapterOnclickListener = listener;
  }

  @Override
  public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    LayoutInflater layoutInflater
        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.video_item_full, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position)
  {
    VideoItem videoItem = videoList.get(position);
    holder.txtvTitle.setText(videoItem.getTitle());
    holder.txtvSubTitle.setText(videoItem.getChannelTitle());
    holder.txtvTime.setText(videoItem.getDuration());
    Glide.with(context)
         .load(videoItem.getThumbnailUrl())
         .into(holder.imgvThumbnail);
  }

  @Override
  public int getItemCount()
  {
    return videoList.size();
  }

  public interface RecyclerViewHomeAdapterOnclickListener
  {
    void onItemClick(View v, int position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    @BindView (R.id.video_item_full_iv_thumbnail)
    ImageView imgvThumbnail;
    @BindView (R.id.video_item_full_tv_title)
    TextView  txtvTitle;
    @BindView (R.id.video_item_full_tv_statistics)
    TextView  txtvSubTitle;
    @BindView (R.id.video_item_full_tv_duration)
    TextView  txtvTime;

    public ViewHolder(View itemView)
    {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick (R.id.video_item_full_iv_thumbnail)
    public void onClickVideo()
    {
      mRecyclerViewHomeAdapterOnclickListener.onItemClick(itemView, getAdapterPosition());
      // int       selectedPosition = getAdapterPosition();
      // VideoItem videoItem        = videoList.get(selectedPosition);
      // Toast.makeText(context, String.valueOf(videoItem.getVideoId()), Toast.LENGTH_SHORT).show();
      // Bundle bundle = new Bundle();
      // bundle.putString("videoId", videoItem.getVideoId());
      // Intent intent = new Intent(context, PlayVideo.class);
      // intent.putExtra("params", bundle);
      // context.startActivity(intent);
    }
  }
}
