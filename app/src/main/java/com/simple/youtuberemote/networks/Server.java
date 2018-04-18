package com.simple.youtuberemote.networks;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.simple.youtuberemote.models.message.AddVideo;
import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.PlaySpecVideo;
import com.simple.youtuberemote.models.message.Type;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

public abstract class Server {
  public final static int PORT = 3456;

  private ServerSocket server;
  private String ip = "";
  private ArrayList<Socket> clients;
  private ArrayList<String> playList;
  private String currentVideo = "";

  public Server() {
    playList = new ArrayList<>();
    clients = new ArrayList<>();
  }

  public void start(Context context) {
    try {
      server = new ServerSocket(PORT);
      WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
      if (wm != null) {
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        onGetIp(ip);
      }
      new Thread(new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              final Socket client = server.accept();
              send(client, new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo)));
              clients.add(client);
              (new Listener(client) {
                @Override
                public void onMessage(Message message) {
                  switch (message.type) {
                    case NEXT:
                      next();
                      broadcastPlaylist();
                      break;
                    case PLAY:
                      onPlay();
                      break;
                    case PAUSE:
                      onPause();
                      break;
                    case PLAY_SPEC:
                      PlaySpecVideo playSpecVideo = (PlaySpecVideo) message.data;
                      onVideoChange(playSpecVideo.videoId);
                      break;
                    case ADD_VIDEO:
                      AddVideo addVideo = (AddVideo) message.data;
                      playList.add(addVideo.videoId);
                      onPlayListFilled();
                      break;
                    default:
                      break;
                  }
                }
              }).start();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void broadcastPlaylist() {
    try {
      Message message = new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo));
      for (Socket client: clients) {
        send(client, message);
      }
    }
    catch (Exception e) {
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

  private void peek() {
    if (playList.size() == 0) {
      currentVideo = "";
      onPlayListEmpty();
      return;
    }
    currentVideo = playList.get(0);
    onVideoChange(currentVideo);
  }

  public void next() {
    playList.remove(currentVideo);
    peek();
  }

  public abstract void onGetIp(String ip);
  public abstract void onPlayListFilled();
  public abstract void onPlayListEmpty();
  public abstract void onVideoChange(String id);
  public abstract void onPause();
  public abstract void onPlay();
}
