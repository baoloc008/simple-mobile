package com.simple.youtuberemote.models.API.playlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by loc on 15/04/2018.
 */

public class Id
{

  @SerializedName ("kind")
  @Expose
  private String kind;
  @SerializedName ("videoId")
  @Expose
  private String videoId;

  public String getKind()
  {
    return kind;
  }

  public void setKind(String kind)
  {
    this.kind = kind;
  }

  public String getVideoId()
  {
    return videoId;
  }

  public void setVideoId(String videoId)
  {
    this.videoId = videoId;
  }
}