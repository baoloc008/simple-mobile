package com.simple.youtuberemote.networks;

import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
  private Socket socket;
  private ArrayList<String> playList;
  private String currentVideo;

  public Client(String ip) {
    try {
      socket = new Socket(ip, Server.PORT);
      new Thread(new Runnable() {
        @Override
        public void run(){
          while (true) {
            try {
              ObjectInputStream streamIn = new ObjectInputStream(socket.getInputStream());
              Message message = (Message) streamIn.readObject();
              switch (message.type) {
                case PLAY_LIST:
                  PlayList data = (PlayList) message.data;
                  playList = data.playlist;
                  currentVideo = data.currentVideo;
                  break;
                default:
                  break;
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<String> getPlayList() {
    return playList;
  }

  public String getCurrentVideo() {
    return currentVideo;
  }
}
