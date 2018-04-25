package com.simple.youtuberemote.models;

/**
 * Created by loc on 15/04/2018.
 */

public class VideoItem
{

  private String thumbnailUrl, title, subTitle, videoId, viewCount, duration;

  public VideoItem()
  {
  }

  public VideoItem(String videoId,
                   String title,
                   String subTitle,
                   String thumbnailUrl,
                   String viewCount, String duration)
  {
    this.thumbnailUrl = thumbnailUrl;
    this.title = title;
    this.subTitle = subTitle;
    this.videoId = videoId;
    this.viewCount = viewCount;
    this.duration = duration;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl)
  {
    this.thumbnailUrl = thumbnailUrl;
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
