package com.simple.youtuberemote.models.API.searchvideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by loc on 15/04/2018.
 */

public class Default
{

  @SerializedName ("url")
  @Expose
  private String  url;
  @SerializedName ("width")
  @Expose
  private Integer width;
  @SerializedName ("height")
  @Expose
  private Integer height;

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public Integer getWidth()
  {
    return width;
  }

  public void setWidth(Integer width)
  {
    this.width = width;
  }

  public Integer getHeight()
  {
    return height;
  }

  public void setHeight(Integer height)
  {
    this.height = height;
  }
}