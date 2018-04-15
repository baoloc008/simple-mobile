package com.simple.youtuberemote.models.message;

import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  public Type type;
  public Data data;

  public Message(Type _type, Data _data) {
    type = _type;
    data = _data;
  }
}
