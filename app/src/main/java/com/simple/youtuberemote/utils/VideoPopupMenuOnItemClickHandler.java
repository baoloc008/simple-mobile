package com.simple.youtuberemote.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.simple.youtuberemote.activities.RemoteControlActivity;
import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;


public class VideoPopupMenuOnItemClickHandler
    implements VideoListAdapter.OnItemPopupMenuClickListener
{

  private static final String TAG = VideoPopupMenuOnItemClickHandler.class.getSimpleName();

  Context mContext;

  public VideoPopupMenuOnItemClickHandler(Context context)
  {
    mContext = context;
  }

  @Override
  public void onAddToPlaylist(VideoItem video)
  {
    Log.d(TAG, "Add to playlist: " + video.getTitle());
    try {
      RemoteControlActivity.mClient.addVideo(video.getVideoId());
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onPlay(VideoItem video)
  {
    Log.d(TAG, "Play: " + video.getTitle());
    try {
      RemoteControlActivity.mClient.playVideo(video.getVideoId());
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
    }

  }
}
