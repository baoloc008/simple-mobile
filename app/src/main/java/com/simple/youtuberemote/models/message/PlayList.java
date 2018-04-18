package com.simple.youtuberemote.models.message;

import java.util.ArrayList;

public class PlayList extends Data {
  public ArrayList<String> playlist;
  public String currentVideo;

  public PlayList(ArrayList<String> _playlist, String _currentVideo) {
    playlist = _playlist;
    currentVideo = _currentVideo;
  }
}
