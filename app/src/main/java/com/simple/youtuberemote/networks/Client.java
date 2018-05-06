package com.simple.youtuberemote.networks;

import android.content.Context;
import android.os.Handler;

import com.simple.youtuberemote.models.message.AddVideo;
import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.PlaySpecVideo;
import com.simple.youtuberemote.models.message.Type;
import com.simple.youtuberemote.utils.Utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Client
{
  private static Client instance = null;

  public final static String NO_CONNECTION = "NO_CONNECTION";
  private Socket            socket;
  private ArrayList<String> playList;
  private String            currentVideo;
  private OnPlaylistChange  subscriber;
  private Handler           mHandler;
  private Context context;

  private String serverIp;

  private Client()
  {
    playList = new ArrayList<>();
    mHandler = new Handler();
  }

  public static Client getInstance(Context context) {
    if (instance == null) {
      instance = new Client();
    }
    instance.context = context;
    return instance;
  }

  public void findServer(final Runnable runnable)
  {
    try {
      final DatagramSocket datagramSocket   = new DatagramSocket();
      String               broadcastAddress = Utils.getBroadcastAddress(context);
      InetAddress          inetAddress      = InetAddress.getByName(broadcastAddress);
      String               message          = Server.QUESTION;
      final DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(),
                                                               message.length(),
                                                               inetAddress,
                                                               Server.UDP_PORT);
      new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          boolean done = false;
          while (!done) {
            try {
              datagramSocket.send(datagramPacket);
              DatagramPacket receivedPacket = new DatagramPacket(Server.RESPONSE.getBytes(),
                                                                 Server.RESPONSE.length());
              datagramSocket.receive(receivedPacket);
              String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
              if (response.equals(Server.RESPONSE)) {
                serverIp = receivedPacket.getAddress().getHostName();
                done = true;
              }
            }
            catch (Exception e) {
              // e.printStackTrace();
            }
          }
          datagramSocket.close();
          start();
          mHandler.post(runnable);
        }
      }).start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void start()
  {
    try {
      socket = new Socket(serverIp, Server.TCP_PORT);
      (new Listener(socket)
      {
        @Override
        public void onMessage(Message message)
        {
          switch (message.type) {
            case PLAY_LIST:
              PlayList data = (PlayList) message.data;
              playList = data.playlist;
              currentVideo = data.currentVideo;
              if (subscriber != null) {
                subscriber.onChange(playList, currentVideo);
              }
              break;
            case PLAY:
              if (subscriber != null) {
                subscriber.onPlay();
              }
              break;
            case PAUSE:
              if (subscriber != null) {
                subscriber.onPause();
              }
              break;
            default:
              break;
          }
        }

        @Override
        public void onError()
        {

        }
      }).start();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void send(Socket client, Message message) throws Exception
  {
    if (client == null) {
      throw new Exception(NO_CONNECTION);
    }
    try {
      ObjectOutputStream streamOut = new ObjectOutputStream(client.getOutputStream());
      streamOut.writeObject(message);
      streamOut.flush();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  public ArrayList<String> getPlayList()
  {
    return playList;
  }

  public String getCurrentVideo()
  {
    return currentVideo;
  }

  public void pauseVideo() throws Exception
  {
    send(socket, new Message(Type.PAUSE, null));
  }

  public void playVideo() throws Exception
  {
    send(socket, new Message(Type.PLAY, null));
  }

  public void playVideo(String id) throws Exception
  {
    send(socket, new Message(Type.PLAY_SPEC, new PlaySpecVideo(id)));
  }

  public void addVideo(String id) throws Exception
  {
    send(socket, new Message(Type.ADD_VIDEO, new AddVideo(id)));
  }

  public void setOnPlaylistChange(OnPlaylistChange onPlaylistChange) {
    subscriber = onPlaylistChange;
  }

  public static void close() {
    try {
      if (instance.socket != null)
        instance.socket.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    instance = null;
  }

  public interface OnPlaylistChange {
    void onChange(ArrayList<String> playList, String currentVideo);
    void onPlay();
    void onPause();
  }
}
