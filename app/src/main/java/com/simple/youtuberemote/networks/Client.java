package com.simple.youtuberemote.networks;

import android.content.Context;
import android.util.Log;

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


public abstract class Client
{
  private Socket            socket;
  private ArrayList<String> playList;
  private String            currentVideo;

  private String serverIp;

  public Client(Context context)
  {
    findServer(context);
  }

  private void findServer(Context context)
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
              Log.d("sent", "sent");
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
              e.printStackTrace();
            }
          }
          datagramSocket.close();
          Log.d("Server IP", serverIp);
          start();
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
      addVideo("wKJ9KzGQq0w");
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
              onPlaylistChange(playList, currentVideo);
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

  private void send(Socket client, Message message)
  {
    try {
      ObjectOutputStream streamOut = new ObjectOutputStream(client.getOutputStream());
      streamOut.writeObject(message);
      streamOut.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close()
  {
    try {
      socket.close();
    }
    catch (IOException e) {
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

  public void pauseVideo()
  {
    send(socket, new Message(Type.PAUSE, null));
  }

  public void playVideo()
  {
    send(socket, new Message(Type.PLAY, null));
  }

  public void playVideo(String id)
  {
    send(socket, new Message(Type.PLAY_SPEC, new PlaySpecVideo(id)));
  }

  public void addVideo(String id)
  {
    send(socket, new Message(Type.ADD_VIDEO, new AddVideo(id)));
  }

  public abstract void onPlaylistChange(ArrayList<String> playList, String currentVideo);
}
