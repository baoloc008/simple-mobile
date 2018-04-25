package com.simple.youtuberemote.models;

/**
 * Created by loc on 15/04/2018.
 */

public class VideoItem
{

  private String mVideoId;
  private String mTitle;
  private String mChannelTitle;
  private String mThumbnailUrl;
  private String mViewCount;
  private String mDuration;

  public VideoItem()
  {
  }

  public VideoItem(String videoId,
                   String title,
                   String mChannelTitle,
                   String thumbnailUrl,
                   String viewCount, String duration)
  {
    this.mThumbnailUrl = thumbnailUrl;
    this.mTitle = title;
    this.mChannelTitle = mChannelTitle;
    this.mVideoId = videoId;
    this.mViewCount = viewCount;
    this.mDuration = duration;
  }

  public String getThumbnailUrl()
  {
    return mThumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl)
  {
    this.mThumbnailUrl = thumbnailUrl;
  }

  public String getTitle()
  {
    return mTitle;
  }

  public void setTitle(String title)
  {
    this.mTitle = title;
  }

  public String getChannelTitle()
  {
    return mChannelTitle;
  }

  public void setChannelTitle(String channelTitle)
  {
    this.mChannelTitle = channelTitle;
  }

  public String getVideoId()
  {
    return mVideoId;
  }

  public void setVideoId(String videoId)
  {
    this.mVideoId = videoId;
  }

  public String getViewCount()
  {
    return mViewCount;
  }

  public void setViewCount(String viewCount)
  {
    this.mViewCount = viewCount;
  }

  public String getDuration()
  {
    return mDuration;
  }

  public void setDuration(String duration)
  {
    this.mDuration = duration;
  }
}
