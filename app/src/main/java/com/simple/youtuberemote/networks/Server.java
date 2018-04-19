package com.simple.youtuberemote.networks;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;

import com.simple.youtuberemote.models.message.AddVideo;
import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.PlaySpecVideo;
import com.simple.youtuberemote.models.message.Type;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.Context.WIFI_SERVICE;

public abstract class Server {
  public final static int TCP_PORT = 3456;
  public final static int UDP_PORT = 3457;

  public final static String QUESTION = "ARE YOU TV???";
  public final static String RESPONSE = "YES, I AM";

  private ServerSocket   server;
  private DatagramSocket mDatagramSocket;
  private byte[] buf = new byte[256];
  private Handler        mHandler;
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
      WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
      if (wm != null) {
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        onGetIp(ip);
      }
      mDatagramSocket = new DatagramSocket(UDP_PORT);
      server = new ServerSocket(TCP_PORT);
      mHandler = new Handler();
      new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
          try {
            mDatagramSocket.receive(receivedPacket);
            String message = new String(receivedPacket.getData());
            if (message.equals(QUESTION)) {
              DatagramPacket sendPacket = new DatagramPacket(RESPONSE.getBytes(),
                                                             RESPONSE.length(),
                                                             receivedPacket.getAddress(),
                                                             receivedPacket.getPort());
              mDatagramSocket.send(sendPacket);
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
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
                public void onMessage(final Message message) {
                  Log.d("Message", message.toString());
                  switch (message.type) {
                    case NEXT:
                      mHandler.post(new Runnable()
                      {
                        @Override
                        public void run()
                        {
                          next();
                        }
                      });
                      broadcastPlaylist();
                      break;
                    case PLAY:
                      mHandler.post(new Runnable()
                      {
                        @Override
                        public void run()
                        {
                          onPlay();
                        }
                      });
                      break;
                    case PAUSE:
                      mHandler.post(new Runnable()
                      {
                        @Override
                        public void run()
                        {
                          onPause();
                        }
                      });
                      break;
                    case PLAY_SPEC:
                      final PlaySpecVideo playSpecVideo = (PlaySpecVideo) message.data;
                      mHandler.post(new Runnable()
                      {
                        @Override
                        public void run()
                        {
                          onVideoChange(playSpecVideo.videoId);
                        }
                      });
                      break;
                    case ADD_VIDEO:
                      AddVideo addVideo = (AddVideo) message.data;
                      playList.add(addVideo.videoId);
                      if (playList.size() == 1) {
                        mHandler.post(new Runnable()
                        {
                          @Override
                          public void run()
                          {
                            onPlayListFilled();
                          }
                        });
                      }
                      broadcastPlaylist();
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
  public void close() {
    mDatagramSocket.close();
    try {
      server.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  public abstract void onGetIp(String ip);
  public abstract void onPlayListFilled();
  public abstract void onPlayListEmpty();
  public abstract void onVideoChange(String id);
  public abstract void onPause();
  public abstract void onPlay();
}
