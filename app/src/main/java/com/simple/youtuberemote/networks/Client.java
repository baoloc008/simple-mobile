package com.simple.youtuberemote.networks;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
  private Socket socket;
  private ArrayList<String> playList;

  public Client(String ip) {
    try {
      socket = new Socket(ip, Server.PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<String> getPlayList() {
    return playList;
  }
}
