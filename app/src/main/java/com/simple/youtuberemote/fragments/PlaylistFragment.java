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
import com.simple.youtuberemote.activities.RemoteControlActivity;
import com.simple.youtuberemote.adapters.PlaylistAdapter;
import com.simple.youtuberemote.models.API.videodetail.VideoDetail;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.Client;
import com.simple.youtuberemote.networks.retrofit.APIUtils;
import com.simple.youtuberemote.networks.retrofit.DataClient;

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

public class PlaylistFragment extends Fragment
    implements PlaylistAdapter.PlaylistAdapterOnClickListener
{

  @BindView (R.id.recycleViewPlaylist)
  RecyclerView recyclerViewPlaylist;

  private PlaylistAdapter playlistAdapter;
  private List<VideoItem> videoList;
  private DataClient      dataClient;

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

    fetchVideoList(RemoteControlActivity.mClient.getPlayList());

    RemoteControlActivity.mClient.setOnPlaylistChange(new Client.OnPlaylistChange()
    {
      @Override
      public void onChange(ArrayList<String> playList, String currentVideo)
      {
        fetchVideoList(playList);
      }

      @Override
      public void onPlay()
      {

      }

      @Override
      public void onPause()
      {

      }
    });

    return view;
  }

  @Override
  public void onItemClick(View v, int position)
  {

  }

  private void fetchVideoList(ArrayList<String> listId)
  {
    videoList.clear();
    for (String videoId : listId) {
      dataClient.getVideoDetail(videoId).enqueue(new Callback<VideoDetail>()
      {
        @Override
        public void onResponse(@NonNull Call<VideoDetail> call,
                               @NonNull Response<VideoDetail> response)
        {
          if (response.isSuccessful()) {
            VideoDetail videoDetail = response.body();
            VideoItem   videoItem   = new VideoItem();
            videoItem.setThumbnailUrl(videoDetail.getItems()
                                                 .get(0)
                                                 .getSnippet()
                                                 .getThumbnails()
                                                 .getMedium()
                                                 .getUrl());
            videoItem.setTitle(videoDetail.getItems().get(0).getSnippet().getTitle());
            videoItem.setChannelTitle(videoDetail.getItems().get(0).getSnippet().getChannelTitle());
            videoList.add(videoItem);
            playlistAdapter.notifyDataSetChanged();
          }
          else {
            Toast.makeText(getActivity(),
                           String.valueOf("Error while get videoDetail:" + response.code()),
                           Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(@NonNull Call<VideoDetail> call, @NonNull Throwable t)
        {

        }
      });
    }
  }
}
