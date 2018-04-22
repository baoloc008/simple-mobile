package com.simple.youtuberemote.models;

/**
 * Created by loc on 15/04/2018.
 */

public class VideoItem
{
  private String thumbnail, title, subTitle, videoId, viewCount, duration;

  public VideoItem()
  {
  }

  public VideoItem(String thumbnail,
                   String title,
                   String subTitle,
                   String videoId,
                   String viewCount, String duration)
  {
    this.thumbnail = thumbnail;
    this.title = title;
    this.subTitle = subTitle;
    this.videoId = videoId;
    this.viewCount = viewCount;
    this.duration = duration;
  }

  public String getThumbnail()
  {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail)
  {
    this.thumbnail = thumbnail;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getSubTitle()
  {
    return subTitle;
  }

  public void setSubTitle(String subTitle)
  {
    this.subTitle = subTitle;
  }

  public String getVideoId()
  {
    return videoId;
  }

  public void setVideoId(String videoId)
  {
    this.videoId = videoId;
  }

  public String getViewCount()
  {
    return viewCount;
  }

  public void setViewCount(String viewCount)
  {
    this.viewCount = viewCount;
  }

  public String getDuration()
  {
    return duration;
  }

  public void setDuration(String duration)
  {
    this.duration = duration;
  }
}