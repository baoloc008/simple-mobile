package com.simple.youtuberemote.networks;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class YoutubeApiHelper
{

  private YouTube             mYoutube;
  private YouTube.Search.List mQuery;

  public YoutubeApiHelper(Context context)
  {

    mYoutube = new YouTube.Builder(new NetHttpTransport(),
                                   new JacksonFactory(),
                                   new HttpRequestInitializer()
                                   {
                                     @Override
                                     public void initialize(HttpRequest request) throws IOException
                                     {

                                     }
                                   }).setApplicationName(context.getString(R.string.app_name))
                                     .build();

    try {
      mQuery = mYoutube.search().list("id,snippet");
      mQuery.setKey(Config.REMOTE_API_KEY);
      mQuery.setType("video");
      mQuery.setFields(
          "items(id/videoId,snippet(channelTitle,thumbnails/default/url,title))");
    }
    catch (IOException e) {
      Log.d("YC", "Could not initialize: " + e);
    }
  }

  public List<VideoItem> search(String queryTem)
  {
    mQuery.setQ(queryTem);
    try {
      SearchListResponse response = mQuery.execute();
      List<SearchResult> results  = response.getItems();

      List<VideoItem> items = new ArrayList<>();
      for (SearchResult result : results) {
        String    videoId      = result.getId().getVideoId();
        String    title        = result.getSnippet().getTitle();
        String    subTitle     = result.getSnippet().getChannelTitle();
        String    thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
        VideoItem item         = new VideoItem(videoId, title, subTitle, thumbnailUrl, null, null);
        items.add(item);
      }
      return items;
    }
    catch (IOException e) {
      Log.d("YC", "Could not search: " + e);
      return null;
    }
  }
}
