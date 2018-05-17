package com.simple.youtuberemote.models.message;

public class AddVideo extends Data
{
  public String videoId;

  public AddVideo(String id)
  {
    videoId = id;
  }
}
