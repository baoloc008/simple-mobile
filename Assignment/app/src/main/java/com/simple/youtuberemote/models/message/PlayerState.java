package com.simple.youtuberemote.models.message;

public class PlayerState extends Data
{
  public boolean isPlaying;

  public PlayerState(boolean _isPlaying)
  {
    isPlaying = _isPlaying;
  }
}
