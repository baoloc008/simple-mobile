package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.activities.RemoteControlActivity;
import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.Client;
import com.simple.youtuberemote.networks.YoutubeApi.FetchVideoDetailTask;
import com.simple.youtuberemote.networks.YoutubeApi.YoutubeApiHelper;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by loc on 15/04/2018.
 */

public class PlaylistFragment extends Fragment
{

  @BindView (R.id.recycleViewPlaylist)
  EasyRecyclerView recyclerViewPlaylist;

  private VideoListAdapter mResultVideoListAdapter;

  private FetchVideoDetailTask mFetchTask = YoutubeApiHelper.fetchVideoDetail();

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_playlist, container, false);
    ButterKnife.bind(this, view);

    initResultVideoListView();
    fetch(RemoteControlActivity.mClient.getPlayList());

    RemoteControlActivity.mClient.setOnPlaylistChange(new Client.OnPlaylistChange()
    {
      @Override
      public void onChange(ArrayList<String> playList, String currentVideo)
      {
        fetch(playList);
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
  private void fetch(ArrayList<String> playlist) {
    List<VideoItem> detailResults = mFetchTask.fetch(playlist);
    mResultVideoListAdapter.addAll(detailResults);
  }
  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(getContext(), VideoListAdapter.COMPACT_VIEW_TYPE,
                                                   new VideoPopupMenuOnItemClickHandler(getContext()));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                                                                            DividerItemDecoration.VERTICAL);
    recyclerViewPlaylist.setLayoutManager(layoutManager);
    recyclerViewPlaylist.addItemDecoration(dividerItemDecoration);
    recyclerViewPlaylist.setAdapterWithProgress(mResultVideoListAdapter);

    mResultVideoListAdapter.setError(R.layout.rv_error, new RecyclerArrayAdapter.OnErrorListener()
    {
      @Override
      public void onErrorShow()
      {
        mResultVideoListAdapter.resumeMore();
      }

      @Override
      public void onErrorClick()
      {
        mResultVideoListAdapter.resumeMore();
      }
    });

    mResultVideoListAdapter.clear();
    mResultVideoListAdapter.pauseMore();
  }
}
