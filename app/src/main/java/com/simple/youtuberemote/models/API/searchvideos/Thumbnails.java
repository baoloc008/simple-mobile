package com.simple.youtuberemote.models.API.searchvideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by loc on 15/04/2018.
 */

public class Thumbnails
{

  @SerializedName ("default")
  @Expose
  private Default _default;
  @SerializedName ("medium")
  @Expose
  private Medium  medium;
  @SerializedName ("high")
  @Expose
  private High    high;

  public Default getDefault()
  {
    return _default;
  }

  public void setDefault(Default _default)
  {
    this._default = _default;
  }

  public Medium getMedium()
  {
    return medium;
  }

  public void setMedium(Medium medium)
  {
    this.medium = medium;
  }

  public High getHigh()
  {
    return high;
  }

  public void setHigh(High high)
  {
    this.high = high;
  }
}