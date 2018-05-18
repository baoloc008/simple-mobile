package com.simple.youtuberemote.utils;

import android.content.Context;
import android.widget.Toast;

import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.Client;


public class VideoPopupMenuOnItemClickHandler
    implements VideoListAdapter.OnItemPopupMenuClickListener
{

  private static final String TAG = VideoPopupMenuOnItemClickHandler.class.getSimpleName();
  Context mContext;
  private Client mClient;

  public VideoPopupMenuOnItemClickHandler(Context context)
  {
    mContext = context;
    mClient = Client.getInstance(context);
  }

  @Override
  public void onAddToPlaylist(VideoItem video)
  {
    try {
      mClient.addVideo(video.getVideoId());
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onPlay(VideoItem video)
  {
    try {
      mClient.playVideo(video.getVideoId());
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onDelete(VideoItem video)
  {
    try {
      mClient.removeVideo(video.getVideoId());
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
    }
  }
}
