package com.simple.youtuberemote.networks.YoutubeApi;

import android.os.Handler;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class FetchVideoDetailTask
{

  private static final String TAG = FetchVideoDetailTask.class.getSimpleName();

  private static Handler      mHandler;
  private static List<String> mVideoIdList;
  private static Callback     mCallback;

  public FetchVideoDetailTask()
  {
    mHandler = new Handler();
  }

  public List<VideoItem> fetch(List<String> videoIdList)
  {
    if (videoIdList == null || videoIdList.size() == 0) {
      return new ArrayList<>();
    }
    reset(videoIdList, null);
    return execute();
  }

  public void fetchAsync(List<String> videoIdList, Callback callback)
  {
    if (videoIdList == null || videoIdList.size() == 0) {
      return;
    }
    reset(videoIdList, callback);
    asyncExecute();
  }

  private void reset(List<String> videoIdList, Callback callback)
  {
    mVideoIdList = videoIdList;
    mCallback = callback;
  }

  private List<VideoItem> execute()
  {
    try {
      YouTube.Videos.List videoListRequest = YoutubeApiHelper.get().videos()
                                                             .list(
                                                                 "snippet,contentDetails,statistics");
      videoListRequest.setKey(Config.REMOTE_API_KEY);
      videoListRequest.setFields(
          "items(contentDetails/duration,id,snippet(channelTitle,thumbnails/default,title),statistics/viewCount)");

      StringBuilder stringBuilder = new StringBuilder();
      for (String id : mVideoIdList) {
        stringBuilder.append(id);
        stringBuilder.append(",");
      }
      String joinedVideoIdList = stringBuilder.toString();
      joinedVideoIdList = joinedVideoIdList.substring(0, joinedVideoIdList.length() - 1);
      videoListRequest.setId(joinedVideoIdList);

      VideoListResponse response = videoListRequest.execute();
      List<Video>       results  = response.getItems();

      List<VideoItem> items = new ArrayList<>();
      for (Video result : results) {
        String     videoId      = result.getId();
        String     title        = result.getSnippet().getTitle();
        String     channelTitle = result.getSnippet().getChannelTitle();
        String     thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
        BigInteger viewCount    = result.getStatistics().getViewCount();
        String     duration     = result.getContentDetails().getDuration();
        VideoItem item = new VideoItem(videoId,
                                       title,
                                       channelTitle,
                                       thumbnailUrl,
                                       viewCount,
                                       duration);
        items.add(item);
      }
      return items;
    }
    catch (IOException e) {
      return null;
    }
  }

  private void asyncExecute()
  {
    new Thread()
    {
      public void run()
      {
        final List<VideoItem> results = execute();
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mCallback.onFetchComplete(results != null, results);
          }
        });
      }
    }.start();
  }

  public interface Callback
  {

    void onFetchComplete(boolean ok, List<VideoItem> result);
  }
}
