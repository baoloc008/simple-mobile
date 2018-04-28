package com.simple.youtuberemote.networks;

import java.util.ArrayList;
import java.util.List;


public class SuggestionApiHelper
{

  private static final String BASE_URL = "http://suggestqueries.google.com/complete/search";

  private static final String PARAMETER_CLIENT = "client";
  private static final String PARAMETER_QUERY  = "q";
  private static final String PARAMETER_DS     = "yt";

  public List<String> getSuggestions(String query)
  {
    return new ArrayList<>();
  }
}
