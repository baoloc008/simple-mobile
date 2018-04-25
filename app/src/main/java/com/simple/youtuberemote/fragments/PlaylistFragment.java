package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.PlaylistAdapter;
import com.simple.youtuberemote.models.API.searchvideos.Item;
import com.simple.youtuberemote.models.API.searchvideos.SearchVideos;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.retrofit.APIUtils;
import com.simple.youtuberemote.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by loc on 15/04/2018.
 */

public class PlaylistFragment extends Fragment implements PlaylistAdapter.PlaylistAdapterOnClickListener
{
  @BindView(R.id.recycleViewPlaylist)
  RecyclerView recyclerViewPlaylist;

  private PlaylistAdapter playlistAdapter;
  private List<VideoItem> videoList;
  private DataClient dataClient;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_playlist, container, false);
    ButterKnife.bind(this, view);

    videoList = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    playlistAdapter = new PlaylistAdapter(getActivity(), videoList, this);
    recyclerViewPlaylist.setLayoutManager(layoutManager);
    recyclerViewPlaylist.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                     DividerItemDecoration.VERTICAL));
    recyclerViewPlaylist.setAdapter(playlistAdapter);

    dataClient = APIUtils.getData();
    dataClient.getSearchVideos("doremon").enqueue(new Callback<SearchVideos>()
    {
      @Override
      public void onResponse(@NonNull Call<SearchVideos> call, @NonNull Response<SearchVideos> response)
      {
        if (response.isSuccessful()) {
          SearchVideos searchVideos = response.body();
          if (searchVideos != null)
          {
            List<Item> videoAPIList = searchVideos.getItems();
            for (Item item : videoAPIList)
            {
              VideoItem videoItem = new VideoItem();
              videoItem.setThumbnail(item.getSnippet().getThumbnails().getMedium().getUrl());
              videoItem.setTitle(item.getSnippet().getTitle());
              videoItem.setSubTitle(item.getSnippet().getChannelTitle());
              videoList.add(videoItem);
              playlistAdapter.notifyDataSetChanged();
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
      public void onFailure(@NonNull Call<SearchVideos> call, @NonNull Throwable t)
      {

      }
    });

    return view;
  }

  @Override
  public void onItemClick(View v, int position)
  {

  }
}
