package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
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
import butterknife.OnClick;


/**
 * Created by loc on 15/04/2018.
 */

public class PlaylistFragment extends Fragment
{

  @BindView (R.id.recycleViewPlaylist)
  EasyRecyclerView recyclerViewPlaylist;

  @BindView (R.id.playlist_ib_skip_previous)
  ImageButton mSkipPreviousButton;
  @BindView (R.id.playlist_ib_play)
  ImageButton mPlayButton;
  @BindView (R.id.playlist_ib_skip_next)
  ImageButton mSkipNextButton;
  private Handler          mHandler;
  private VideoListAdapter mResultVideoListAdapter;
  private Client           mClient;

  private FetchVideoDetailTask mFetchTask = YoutubeApiHelper.fetchVideoDetail();

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_playlist, container, false);
    ButterKnife.bind(this, view);
    mHandler = new Handler();
    initResultVideoListView();
    mClient = Client.getInstance(getContext());
    fetch(mClient.getPlayList());
    setIconPlayButton(mClient.isPlaying());

    mClient.setOnPlaylistChange(new Client.OnPlaylistChange()
    {
      @Override
      public void onChange(ArrayList<String> playList, String currentVideo)
      {
        fetch(playList);
      }

      @Override
      public void onPlayerStateChange(boolean _isPlaying)
      {
        setIconPlayButton(_isPlaying);
      }
    });

    return view;
  }

  private void setIconPlayButton(final boolean _isPlaying)
  {
    mHandler.post(new Runnable()
    {
      @Override
      public void run()
      {
        mPlayButton.setImageResource(_isPlaying
                                     ? R.drawable.ic_video_player_pause
                                     : R.drawable.ic_video_player_play);
      }
    });
  }

  private void fetch(ArrayList<String> playlist)
  {
    final List<VideoItem> detailResults = mFetchTask.fetch(playlist);
    mHandler.post(new Runnable()
    {
      @Override
      public void run()
      {
        mResultVideoListAdapter.clear();
        mResultVideoListAdapter.addAll(detailResults);
      }
    });
  }

  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(getContext(),
                                                   VideoListAdapter.COMPACT_VIEW_TYPE,
                                                   VideoListAdapter.POPUP_MEMNU_PLAYLIST,
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

  @OnClick (R.id.playlist_ib_skip_previous)
  void onSkipPreviousButtonClick()
  {
    Toast.makeText(getContext(), "Skip Previous clicked.", Toast.LENGTH_LONG).show();
  }

  @OnClick (R.id.playlist_ib_play)
  void onPlayButtonClick()
  {
    try {
      mClient.togglePlayerState();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnClick (R.id.playlist_ib_skip_next)
  void onSkipNextButtonClick()
  {
    try {
      mClient.next();
    }
    catch (Exception e) {
      Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
    }
  }
}
