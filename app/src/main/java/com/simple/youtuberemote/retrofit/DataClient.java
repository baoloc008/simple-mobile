package com.simple.youtuberemote.retrofit;

import com.simple.youtuberemote.models.API.searchvideos.SearchVideos;
import com.simple.youtuberemote.models.API.videodetail.VideoDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by loc on 15/04/2018.
 */

public interface DataClient
{
  @GET ("search?part=snippet&type=video&maxResults=50&key=AIzaSyC1aaCns4XPk3iXSgVxG4cVT5t-BbSeemM")
  Call<SearchVideos> getSearchVideos(@Query ("q") String keyword);

  @GET ("videos?part=snippet,contentDetails,statistics&key=AIzaSyC1aaCns4XPk3iXSgVxG4cVT5t-BbSeemM")
  Call<VideoDetail> getVideoDetail(@Query ("id") String videoId);

}