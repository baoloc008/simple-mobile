package com.simple.youtuberemote.models.API.video;

/**
 * Created by loc on 4/22/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Item
{

  @SerializedName ("kind")
  @Expose
  private String         kind;
  @SerializedName ("etag")
  @Expose
  private String         etag;
  @SerializedName ("id")
  @Expose
  private String         id;
  @SerializedName ("contentDetails")
  @Expose
  private ContentDetails contentDetails;
  @SerializedName ("statistics")
  @Expose
  private Statistics     statistics;

  public String getKind()
  {
    return kind;
  }

  public void setKind(String kind)
  {
    this.kind = kind;
  }

  public String getEtag()
  {
    return etag;
  }

  public void setEtag(String etag)
  {
    this.etag = etag;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public ContentDetails getContentDetails()
  {
    return contentDetails;
  }

  public void setContentDetails(ContentDetails contentDetails)
  {
    this.contentDetails = contentDetails;
  }

  public Statistics getStatistics()
  {
    return statistics;
  }

  public void setStatistics(Statistics statistics)
  {
    this.statistics = statistics;
  }
}