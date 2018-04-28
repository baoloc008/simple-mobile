package com.simple.youtuberemote.networks.SuggestionApi;

import java.util.List;


public class SuggestionResponse
{

  private String       mQuery;
  private List<String> mSuggestions;

  public SuggestionResponse(String query, List<String> suggestions)
  {
    mQuery = query;
    mSuggestions = suggestions;
  }

  public String getQuery()
  {
    return mQuery;
  }

  public List<String> getSuggestions()
  {
    return mSuggestions;
  }
}
