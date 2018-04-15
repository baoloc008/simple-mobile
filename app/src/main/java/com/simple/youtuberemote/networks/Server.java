package com.simple.youtuberemote.networks;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.Type;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

public class Server {
  public final static int PORT = 3456;

  private ServerSocket server;
  private String ip;
  private ArrayList<Socket> clients;
  private ArrayList<String> playList;
  private String currentVideo;

  public Server() {
    playList = new ArrayList<>();
    clients = new ArrayList<>();
  }

  public void start(Context context) {
    try {
      server = new ServerSocket(PORT);
      WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
      ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
      new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              Socket client = server.accept();
              send(client, new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo)));
              clients.add(client);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }).start();
    } catch (IOException e) {
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

  public String getIp() {
    return ip;
  }

  public String peek() throws Exception {
    if (playList.size() == 0) {
      throw new Exception("EMPTY_PLAYLIST");
    }
    currentVideo = playList.get(0);
    return currentVideo;
  }

  public String next() throws Exception {
    playList.remove(currentVideo);
    return peek();
  }


}
