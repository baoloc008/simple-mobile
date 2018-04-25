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


public class Client
{
  public final static String NO_CONNECTION = "NO_CONNECTION";
  private Socket            socket;
  private ArrayList<String> playList;
  private String            currentVideo;
  private OnPlaylistChange subscriber;

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

  public void close()
  {
    try {
      if (socket != null)
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

  public interface OnPlaylistChange {
    public void onChange(ArrayList<String> playList, String currentVideo);
  }
}
