package com.simple.youtuberemote.networks;

import android.util.Log;

import com.simple.youtuberemote.models.message.Message;

import java.io.ObjectInputStream;
import java.net.Socket;


public abstract class Listener extends Thread
{
  private Socket socket;

  public Listener(Socket _socket)
  {
    socket = _socket;
  }

  public void run()
  {
    while (true) {
      try {
        ObjectInputStream streamIn = new ObjectInputStream(socket.getInputStream());
        Message           message  = (Message) streamIn.readObject();
        Log.d("Message", message.type.toString());
        onMessage(message);
      }
      catch (Exception e) {
        e.printStackTrace();
        onError();
        break;
      }
    }
  }

  public abstract void onMessage(Message message);
  public abstract void onError();
}
