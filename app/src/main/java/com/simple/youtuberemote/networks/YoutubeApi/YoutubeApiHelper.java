package com.simple.youtuberemote.networks.YoutubeApi;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.simple.youtuberemote.utils.Config;

import java.io.IOException;


public class YoutubeApiHelper
{

  private static final int CONNECTION_TIMEOUT = 5000;
  private static final int READ_TIMEOUT       = 60000;

  private static YoutubeApiHelper mInstance;

  private static FetchVideoDetailTask mFetchVideoDetailTask;
  private static SearchYoutubeTask    mSearchTask;
  private static GetTrendVideoTask    mGetTrendVideoTask;

  private static YouTube mYoutube;

  static {
    mInstance = new YoutubeApiHelper();
  }

  private YoutubeApiHelper()
  {
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
                                   }).setApplicationName(Config.APP_NAME)
                                     .build();
    mFetchVideoDetailTask = new FetchVideoDetailTask();
    mSearchTask = new SearchYoutubeTask();
    mGetTrendVideoTask = new GetTrendVideoTask();
  }

  public static YoutubeApiHelper getInstance()
  {
    return mInstance;
  }

  static YouTube get()
  {
    return mYoutube;
  }

  public static FetchVideoDetailTask fetchVideoDetail()
  {
    return mFetchVideoDetailTask;
  }

  public static SearchYoutubeTask searchYoutube()
  {
    return mSearchTask;
  }

  public static GetTrendVideoTask getTrendVideoTask()
  {
    return mGetTrendVideoTask;
  }
}
