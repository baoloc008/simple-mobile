package com.simple.youtuberemote.networks;

import com.simple.youtuberemote.models.message.AddVideo;
import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.PlaySpecVideo;
import com.simple.youtuberemote.models.message.Type;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Client {
  private Socket            socket;
  private ArrayList<String> playList;
  private String            currentVideo;

  public Client(String ip) {
    try {
      socket = new Socket(ip, Server.PORT);
      (new Listener(socket) {
        @Override
        public void onMessage(Message message) {
          switch (message.type) {
            case PLAY_LIST:
              PlayList data = (PlayList) message.data;
              playList = data.playlist;
              currentVideo = data.currentVideo;
              break;
            default:
              break;
          }
        }
      }).start();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void send(Socket client, Message message) {
    try {
      ObjectOutputStream streamOut = new ObjectOutputStream(client.getOutputStream());
      streamOut.writeObject(message);
      streamOut.flush();
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
  public void pauseVideo() {
    send(socket, new Message(Type.PAUSE, null));
  }
  public void playVideo() {
    send(socket, new Message(Type.PLAY, null));
  }
  public void playVideo(String id) {
    send(socket, new Message(Type.PLAY_SPEC, new PlaySpecVideo(id)));
  }
  public void addVideo(String id) {
    send(socket, new Message(Type.ADD_VIDEO, new AddVideo(id)));
  }
}
