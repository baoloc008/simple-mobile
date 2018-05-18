package com.simple.youtuberemote.networks.YoutubeApi;

import android.os.Handler;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetTrendVideoTask
{
  private final String TAG = GetTrendVideoTask.class.getSimpleName();
  private Handler  mHandler;
  private String   mNextPageToken;
  private Callback mCallback;

  public GetTrendVideoTask()
  {
    mHandler = new Handler();
  }

  public void getTrendVideoAsync(Callback callback)
  {
    reset(callback);
    asyncExecute();
  }

  public void getNextTrendVideoAsync()
  {
    asyncExecute();
  }

  private void reset(Callback callback)
  {
    mCallback = callback;
    setNextPageToken(null);
  }

  private void asyncExecute()
  {
    new Thread()
    {
      public void run()
      {
        final List<String> results = execute();
        mHandler.post(new Runnable()
        {
          public void run()
          {
            if (mCallback != null) {
              mCallback.onGetTrendVideoComplete(results != null, results);
            }
          }
        });
      }
    }.start();
  }

  private List<String> execute()
  {
    try {
      YouTube.Videos.List getTrendVideoRequest = YoutubeApiHelper.get().videos().list("snippet");
      String              nextPageToken        = getNextPageToken();

      getTrendVideoRequest.setKey(Config.REMOTE_API_KEY);
      getTrendVideoRequest.setMaxResults((long) 5);
      if (nextPageToken != null) {
        getTrendVideoRequest.setPageToken(nextPageToken);
      }
      getTrendVideoRequest.setFields("items/id,nextPageToken");
      getTrendVideoRequest.setChart("mostPopular");
      getTrendVideoRequest.setRegionCode("VN");

      VideoListResponse response = getTrendVideoRequest.execute();
      setNextPageToken(response.getNextPageToken());
      List<Video> results = response.getItems();

      List<String> items = new ArrayList<>();
      for (Video result : results) {
        String videoId = result.getId();
        items.add(videoId);
      }
      return items;
    }
    catch (IOException e) {
      return null;
    }
  }

  private synchronized String getNextPageToken()
  {
    return mNextPageToken;
  }

  private synchronized void setNextPageToken(String token)
  {
    mNextPageToken = token;
  }

  public interface Callback
  {
    void onGetTrendVideoComplete(boolean ok, List<String> result);
  }
}
