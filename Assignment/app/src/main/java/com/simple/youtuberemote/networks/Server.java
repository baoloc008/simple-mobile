package com.simple.youtuberemote.networks;

import android.os.Handler;

import com.simple.youtuberemote.models.message.AddVideo;
import com.simple.youtuberemote.models.message.Message;
import com.simple.youtuberemote.models.message.PlayList;
import com.simple.youtuberemote.models.message.PlaySpecVideo;
import com.simple.youtuberemote.models.message.PlayerState;
import com.simple.youtuberemote.models.message.RemoveVideo;
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
  private boolean        running;
  private byte[] buf = new byte[256];
  private Handler           mHandler;
  private ArrayList<Socket> clients;
  private ArrayList<String> playList;
  private String  currentVideo = "";
  private boolean isPlaying    = false;

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
              send(client, new Message(Type.PLAYER_STATE, new PlayerState(isPlaying)));
              clients.add(client);
              mHandler.post(new Runnable()
              {
                @Override
                public void run()
                {
                  onClientChange(clients.size());
                }
              });
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
                      isPlaying = true;
                      broadcastPlayerState();
                      break;
                    case PREVIOUS:
                      mHandler.post(new Runnable()
                      {
                        @Override
                        public void run()
                        {
                          previous();
                        }
                      });
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
                      isPlaying = true;
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
                      isPlaying = false;
                      break;
                    case PLAY_SPEC:
                      final PlaySpecVideo playSpecVideo = (PlaySpecVideo) message.data;
                      currentVideo = playSpecVideo.videoId;
                      if (playList.contains(currentVideo)) {
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
                            if (playList.isEmpty()) {
                              onPlayListFilled();
                            }
                            playList.add(currentVideo);
                            onVideoChange(currentVideo);
                          }
                        });
                      }
                      isPlaying = true;
                      broadcastPlayerState();
                      broadcastPlaylist();
                      break;
                    case ADD_VIDEO:
                      AddVideo addVideo = (AddVideo) message.data;
                      String videoId = addVideo.videoId;
                      if (!playList.contains(videoId)) {
                        playList.add(addVideo.videoId);
                        broadcastPlaylist();
                      }
                      break;
                    case REMOVE_VIDEO:
                      RemoveVideo removeVideo = (RemoveVideo) message.data;
                      playList.remove(removeVideo.videoId);
                      broadcastPlaylist();
                      break;
                    default:
                      break;
                  }
                }

                @Override
                public void onError()
                {
                  clients.remove(client);
                  mHandler.post(new Runnable()
                  {
                    @Override
                    public void run()
                    {
                      onClientChange(clients.size());
                    }
                  });
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

  public void pauseVideo()
  {
    isPlaying = false;
    broadcastPlayerState();
  }

  public void playVideo()
  {
    isPlaying = true;
    broadcastPlayerState();
  }

  public abstract void onClientChange(int count);

  public abstract void onPlayListFilled();

  public abstract void onPlayListEmpty();

  public abstract void onVideoChange(String id);

  public abstract void onPause();

  public abstract void onPlay();

  private void broadcastPlayerState()
  {
    Message message = new Message(Type.PLAYER_STATE, new PlayerState(isPlaying));
    for (Socket client : clients) {
      try {
        send(client, message);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void broadcastPlaylist()
  {
    Message message = new Message(Type.PLAY_LIST, new PlayList(playList, currentVideo));
    for (Socket client : clients) {
      try {
        send(client, message);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
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
    broadcastPlaylist();
  }

  private void previous()
  {
    int index = playList.indexOf(currentVideo);
    if (index > 0) {
      index--;
      currentVideo = playList.get(index);
      onVideoChange(currentVideo);
      broadcastPlaylist();
    }
  }
}
