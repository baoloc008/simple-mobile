package com.simple.youtuberemote.networks;

import android.content.Context;
import android.os.Handler;
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


public abstract class Server
{
  public final static int TCP_PORT = 3456;
  public final static int UDP_PORT = 3457;

  public final static String QUESTION = "ARE YOU TV???";
  public final static String RESPONSE = "YES, I AM";

  private ServerSocket   server;
  private DatagramSocket mDatagramSocket;
  private boolean running;
  private byte[] buf = new byte[256];
  private Handler           mHandler;
  private ArrayList<Socket> clients;
  private ArrayList<String> playList;
  private String currentVideo = "";

  public Server()
  {
    playList = new ArrayList<>();
    clients = new ArrayList<>();
  }

  public void start()
  {
    running = true;
    try {
      mDatagramSocket = new DatagramSocket(UDP_PORT);
      server = new ServerSocket(TCP_PORT);
      mHandler = new Handler();
      new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          while (running) {
            DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
            try {
              mDatagramSocket.receive(receivedPacket);
              String message = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
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
        }
      }).start();
      new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          while (running) {
            try {
              final Socket client = server.accept();
              send(client, new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo)));
              clients.add(client);

              Listener listener = new Listener(client)
              {
                @Override
                public void onMessage(final Message message)
                {
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
                      final String id = playSpecVideo.videoId;
                      if (playList.contains(id)) {
                        mHandler.post(new Runnable()
                        {
                          @Override
                          public void run()
                          {
                            onVideoChange(playSpecVideo.videoId);
                          }
                        });
                      }
                      else {
                        mHandler.post(new Runnable()
                        {
                          @Override
                          public void run()
                          {
                            onPlayListFilled();
                            playList.add(id);
                            onVideoChange(id);
                          }
                        });
                      }
                      break;
                    case ADD_VIDEO:
                      AddVideo addVideo = (AddVideo) message.data;
                      playList.add(addVideo.videoId);
                      break;
                    default:
                      break;
                  }
                  broadcastPlaylist();
                }

                @Override
                public void onError()
                {
                  clients.remove(client);
                }
              };
              listener.start();
            }
            catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }).start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void broadcastPlaylist()
  {
    try {
      Message message = new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo));
      for (Socket client : clients) {
        send(client, message);
      }
    }
    catch (Exception e) {
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

  private void peek()
  {
    if (playList.size() == 0) {
      currentVideo = "";
      onPlayListEmpty();
      return;
    }
    currentVideo = playList.get(0);
    onVideoChange(currentVideo);
  }

  public void next()
  {
    playList.remove(currentVideo);
    peek();
  }

  public void close()
  {
    running = false;
    mDatagramSocket.close();
    try {
      server.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public abstract void onPlayListFilled();

  public abstract void onPlayListEmpty();

  public abstract void onVideoChange(String id);

  public abstract void onPause();

  public abstract void onPlay();
}
