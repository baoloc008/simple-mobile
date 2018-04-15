package com.simple.youtuberemote.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by loc on 15/04/2018.
 */

public interface DataClient {
  @GET("search?part=snippet&type=video&maxResults=50&key=AIzaSyC1aaCns4XPk3iXSgVxG4cVT5t-BbSeemM")
  Call<VideoAPI> getVideo(@Query("q") String keyword);
}