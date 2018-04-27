package com.simple.youtuberemote.networks;

import android.os.Handler;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public final class YoutubeApiHelper
{
  private static final String TAG                = YoutubeApiHelper.class.getSimpleName();
  private static final int    CONNECTION_TIMEOUT = 5000;
  private static final int    READ_TIMEOUT       = 60000;

  private static YouTube mYoutube;

  static {
    mYoutube = new YouTube.Builder(new NetHttpTransport(),
                                   new JacksonFactory(),
                                   new HttpRequestInitializer()
                                   {
                                     @Override
                                     public void initialize(HttpRequest request) throws IOException
                                     {
                                       request.setConnectTimeout(CONNECTION_TIMEOUT);
                                       request.setReadTimeout(READ_TIMEOUT);
                                     }
                                   }).setApplicationName("Youtube Remote")
                                     .build();
  }

  public static List<VideoItem> requestVideosInfoById(List<String> videoIdList)
  {
    try {
      YouTube.Videos.List videoListRequest = mYoutube.videos()
                                                     .list("snippet,contentDetails,statistics");
      videoListRequest.setKey(Config.REMOTE_API_KEY);
      videoListRequest.setFields(
          "items(contentDetails/duration,id,snippet(channelTitle,thumbnails/default,title),statistics/viewCount)");

      StringBuilder stringBuilder = new StringBuilder();
      for (String id : videoIdList) {
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
      Log.d(TAG, "Could not request: " + e);
      return null;
    }
  }

  public static class SearchTask
  {
    private          Handler  mHandler;
    private          Callback mCallback;
    private          String   mQueryTerm;
    private volatile String   mNextPageToken;

    public SearchTask()
    {
      mHandler = new Handler();
    }

    public void search(final String queryTerm, Callback callback)
    {
      execute(queryTerm, callback, false);
    }

    public void searchNext()
    {
      execute(mQueryTerm, mCallback, true);
    }

    private void execute(final String queryTerm, Callback callback, boolean next)
    {
      if (!next) {
        mQueryTerm = queryTerm;
        mCallback = callback;
        setNextPageToken("");
      }

      new Thread()
      {
        public void run()
        {
          Log.d(TAG, "Search Requesting...");

          final List<String> results = search(queryTerm);

          mHandler.post(new Runnable()
          {
            public void run()
            {
              mCallback.onSearchComplete(results != null, results);
            }
          });
        }
      }.start();
    }

    private synchronized String getNextPageToken()
    {
      return mNextPageToken;
    }

    private synchronized void setNextPageToken(String token)
    {
      mNextPageToken = token;
    }

    private List<String> search(String queryTerm)
    {
      try {
        YouTube.Search.List searchRequest = mYoutube.search().list("snippet");

        searchRequest.setKey(Config.REMOTE_API_KEY);
        searchRequest.setMaxResults((long) 5);
        searchRequest.setPageToken(getNextPageToken());
        searchRequest.setType("video");
        searchRequest.setFields("items/id/videoId,nextPageToken");
        searchRequest.setQ(queryTerm);

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
        Log.d(TAG, "Could not search: " + e);
        return null;
      }
    }

    public interface Callback
    {
      void onSearchComplete(boolean ok, List<String> result);
    }
  }
}
