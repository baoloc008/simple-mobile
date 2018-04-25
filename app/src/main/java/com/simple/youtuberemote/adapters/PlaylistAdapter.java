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
 * Created by loc on 4/25/18.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>
{
  private Context                        context;
  private List<VideoItem>                videoList;
  private PlaylistAdapterOnClickListener mAdapterOnClickListener;

  public interface PlaylistAdapterOnClickListener
  {
    void onItemClick(View v, int position);
  }

  public PlaylistAdapter(Context context,
                         List<VideoItem> videoList,
                         PlaylistAdapterOnClickListener adapterOnClickListener)
  {
    this.context = context;
    this.videoList = videoList;
    mAdapterOnClickListener = adapterOnClickListener;
  }

  @Override
  public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View           view = layoutInflater.inflate(R.layout.video_item_playlist, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(PlaylistAdapter.ViewHolder holder, int position)
  {
    VideoItem videoItem = videoList.get(position);
    holder.txtvTitlePlaylist.setText(videoItem.getTitle());
    holder.txtvSubTitlePlaylist.setText(videoItem.getSubTitle());
    Glide.with(context)
         .load(videoItem.getThumbnailUrl())
         .into(holder.imgvThumbnailPlaylist);
  }

  @Override
  public int getItemCount()
  {
    return videoList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    @BindView (R.id.imgvThumbnailPlaylist)
    ImageView imgvThumbnailPlaylist;
    @BindView (R.id.txtvTitlePlaylist)
    TextView  txtvTitlePlaylist;
    @BindView (R.id.txtvSubTitlePlaylist)
    TextView  txtvSubTitlePlaylist;

    public ViewHolder(View itemView)
    {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick (R.id.imgvThumbnailPlaylist)
    public void onClickVideoPlaylist()
    {
      mAdapterOnClickListener.onItemClick(itemView, getAdapterPosition());
    }
  }
}
