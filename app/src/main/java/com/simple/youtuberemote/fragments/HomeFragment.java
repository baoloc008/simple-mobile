package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.activities.MainActivity;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.models.API.Item;
import com.simple.youtuberemote.models.API.VideoAPI;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.retrofit.APIUtils;
import com.simple.youtuberemote.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by loc on 15/04/2018.
 */

public class HomeFragment extends Fragment
{
  private HomeAdapter     homeAdapter;
  private List<VideoItem> videoList;
  private DataClient      dataClient;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View         view             = inflater.inflate(R.layout.fragment_home, container, false);
    RecyclerView recyclerViewHome = view.findViewById(R.id.recycleViewHome);
    videoList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    homeAdapter = new HomeAdapter(getActivity(), videoList);
    recyclerViewHome.setLayoutManager(layoutManager);
    recyclerViewHome.setAdapter(homeAdapter);
    dataClient = APIUtils.getData();
    dataClient.getVideo("phim songoku").enqueue(new Callback<VideoAPI>()
    {
      @Override
      public void onResponse(@NonNull Call<VideoAPI> call, @NonNull Response<VideoAPI> response)
      {
        if (response.isSuccessful()) {
          VideoAPI videoAPI = response.body();
          if (videoAPI != null) {
            List<Item> videoAPIList = videoAPI.getItems();
            for (Item item : videoAPIList) {
              VideoItem videoItem = new VideoItem();
              videoItem.setThumbnail(item.getSnippet().getThumbnails().getMedium().getUrl());
              videoItem.setTitle(item.getSnippet().getTitle());
              videoItem.setSubTitle(item.getSnippet().getChannelTitle());
              videoItem.setVideoId(item.getId().getVideoId());
              videoList.add(videoItem);
              homeAdapter.notifyDataSetChanged();
            }
          }
        }
        else {
          Toast.makeText(getActivity(),
                         String.valueOf("Error:" + response.code()),
                         Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(@NonNull Call<VideoAPI> call, @NonNull Throwable t)
      {
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
    return view;
  }
}
