package com.simple.youtuberemote.models;

import java.math.BigInteger;


/**
 * Created by loc on 15/04/2018.
 */

public class VideoItem
{

  private String     mVideoId;
  private String     mTitle;
  private String     mChannelTitle;
  private String     mThumbnailUrl;
  private BigInteger mViewCount;
  private String     mDuration;
  private boolean    isPlaying;

  public VideoItem()
  {
  }

  public VideoItem(String videoId,
                   String title,
                   String mChannelTitle,
                   String thumbnailUrl,
                   BigInteger viewCount, String duration)
  {
    this.mThumbnailUrl = thumbnailUrl;
    this.mTitle = title;
    this.mChannelTitle = mChannelTitle;
    this.mVideoId = videoId;
    this.mViewCount = viewCount;
    this.mDuration = duration;
    this.isPlaying = false;
  }

  public VideoItem(String videoId,
                   String title,
                   String mChannelTitle,
                   String thumbnailUrl,
                   BigInteger viewCount, String duration,
                   boolean isPlaying)
  {
    this.mThumbnailUrl = thumbnailUrl;
    this.mTitle = title;
    this.mChannelTitle = mChannelTitle;
    this.mVideoId = videoId;
    this.mViewCount = viewCount;
    this.mDuration = duration;
    this.isPlaying = isPlaying;
  }

  public String getThumbnailUrl()
  {
    return mThumbnailUrl;
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

  public String getVideoId()
  {
    return mVideoId;
  }

  public BigInteger getViewCount()
  {
    return mViewCount;
  }

  public String getDuration()
  {
    return mDuration;
  }

  public boolean isPlaying()
  {
    return isPlaying;
  }

  public void setPlaying(boolean playing)
  {
    isPlaying = playing;
  }
}
