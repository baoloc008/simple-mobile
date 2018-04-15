package com.simple.youtuberemote.models.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by loc on 15/04/2018.
 */

public class PageInfo {

  @SerializedName("totalResults")
  @Expose
  private Integer totalResults;
  @SerializedName("resultsPerPage")
  @Expose
  private Integer resultsPerPage;

  public Integer getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }

  public Integer getResultsPerPage() {
    return resultsPerPage;
  }

  public void setResultsPerPage(Integer resultsPerPage) {
    this.resultsPerPage = resultsPerPage;
  }

}