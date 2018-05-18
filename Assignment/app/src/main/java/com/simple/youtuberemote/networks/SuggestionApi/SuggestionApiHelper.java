package com.simple.youtuberemote.networks.SuggestionApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class SuggestionApiHelper
{

  private static final String TAG      = SuggestionApiHelper.class.getSimpleName();
  private static final String BASE_URL = "http://suggestqueries.google.com/complete/";

  private static Service mService;

  static {
    mService = new Retrofit.Builder().baseUrl(BASE_URL)
                                     .addConverterFactory(ScalarsConverterFactory.create())
                                     .build().create(Service.class);
  }

  public static void fetchAsync(String query, final Callback callback)
  {
    if (query.isEmpty()) {
      return;
    }
    mService.fetchSuggestion(query).enqueue(new retrofit2.Callback<String>()
    {
      @Override
      public void onResponse(Call<String> call, Response<String> response)
      {
        boolean      ok          = response.isSuccessful();
        List<String> suggestions = new ArrayList<>();

        if (ok) {
          try {
            JSONArray jsonArray = (new JSONArray(response.body())).getJSONArray(1);
            if (jsonArray != null) {
              for (int i = 0; i < jsonArray.length(); ++i) {
                suggestions.add(jsonArray.getString(i));
              }
            }
          }
          catch (JSONException e) {
            e.printStackTrace();
          }
        }
        else {
          int statusCode = response.code();
        }
        callback.onFetchComplete(ok, suggestions);
      }

      @Override
      public void onFailure(Call<String> call, Throwable t)
      {

      }
    });
  }

  interface Service
  {

    @GET ("search?client=firefox&ds=yt")
    Call<String> fetchSuggestion(@Query ("q") String query);
  }

  public interface Callback
  {

    void onFetchComplete(boolean ok, List<String> suggestions);
  }
}
