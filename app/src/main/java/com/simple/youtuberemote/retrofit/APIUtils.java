package com.simple.youtuberemote.retrofit;

/**
 * Created by loc on 15/04/2018.
 */

public class APIUtils {
  public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";

  public static DataClient getData() {
    return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
  }
}
