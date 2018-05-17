package com.simple.youtuberemote.models.message;

public class RemoveVideo extends Data
{
  public String videoId;

  public RemoveVideo(String id)
  {
    videoId = id;
  }
}
