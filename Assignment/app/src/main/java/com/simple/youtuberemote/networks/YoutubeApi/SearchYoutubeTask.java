package com.simple.youtuberemote.networks.YoutubeApi;

import android.os.Handler;
import android.util.Log;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchYoutubeTask
{

  private final String TAG = SearchYoutubeTask.class.getSimpleName();

  private Handler  mHandler;
  private Callback mCallback;
  private String   mQueryTerm;
  private String   mNextPageToken;

  public SearchYoutubeTask()
  {
    mHandler = new Handler();
  }

  public void searchAsync(final String queryTerm, Callback callback)
  {
    reset(queryTerm, callback);
    asyncExecute();
  }

  public void searchNextAsync()
  {
    asyncExecute();
  }

  public List<String> search(String queryTerm)
  {
    reset(queryTerm, null);
    return execute();
  }

  public List<String> searchNext(String queryTerm)
  {
    return execute();
  }

  private void reset(String queryTerm, Callback callback)
  {
    mQueryTerm = queryTerm;
    mCallback = callback;
    setNextPageToken(null);
  }

  private void asyncExecute()
  {
    new Thread()
    {
      public void run()
      {
        Log.d(TAG, "Search Requesting...");

        final List<String> results = execute();
        Log.d(TAG, "Search Response: " + results);

        mHandler.post(new Runnable()
        {
          public void run()
          {
            if (mCallback != null) {
              mCallback.onSearchComplete(results != null, results);
            }
          }
        });
      }
    }.start();
  }

  private List<String> execute()
  {
    try {
      YouTube.Search.List searchRequest = YoutubeApiHelper.get().search().list("snippet");

      String nextPageToken = getNextPageToken();

      searchRequest.setKey(Config.REMOTE_API_KEY);
      searchRequest.setMaxResults((long) 5);
      if (nextPageToken != null) {
        searchRequest.setPageToken(nextPageToken);
      }
      searchRequest.setType("video");
      searchRequest.setFields("items/id/videoId,nextPageToken");
      searchRequest.setQ(mQueryTerm);

      SearchListResponse response = searchRequest.execute();
      setNextPageToken(response.getNextPageToken());
      List<SearchResult> results = response.getItems();

      List<String> items = new ArrayList<>();
      for (SearchResult result : results) {
        String videoId = result.getId().getVideoId();

        items.add(videoId);
      }
      return items;
    }
    catch (IOException e) {
      Log.d(TAG, "Could not searchAsync: " + e);
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

    void onSearchComplete(boolean ok, List<String> result);
  }
}
