package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.adapters.TrendAdapter;
import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.YoutubeApi.FetchVideoDetailTask;
import com.simple.youtuberemote.networks.YoutubeApi.GetTrendVideoTask;
import com.simple.youtuberemote.networks.YoutubeApi.YoutubeApiHelper;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by loc on 15/04/2018.
 */

public class TrendFragment extends Fragment implements RecyclerArrayAdapter.OnLoadMoreListener
{
  @BindView (R.id.rv_trend_video)
  EasyRecyclerView mRecyclerViewTrendVideo;

  private VideoListAdapter mVideoListAdapter;
  private List<VideoItem> mVideoItems;

  private GetTrendVideoTask mGetTrendVideoTask = YoutubeApiHelper.getTrendVideoTask();
  private FetchVideoDetailTask mFetchTask  = YoutubeApiHelper.fetchVideoDetail();

  private GetTrendVideoTask.Callback mGetTrendVideoCallback = new GetTrendVideoTask.Callback()
  {
    @Override
    public void onGetTrendVideoComplete(boolean ok, List<String> result)
    {
      if (ok)
      {
        mVideoItems = mFetchTask.fetch(result);
        mVideoListAdapter.addAll(mVideoItems);
      }
      else
      {
        mVideoListAdapter.pauseMore();
        mRecyclerViewTrendVideo.showError();
      }
    }
  };

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_trend, container, false);
    ButterKnife.bind(this, view);
    initTrendVideoListView();

    return view;
  }

  private void initTrendVideoListView()
  {
    mVideoListAdapter = new VideoListAdapter(getContext(),
                                             VideoListAdapter.COMPACT_VIEW_TYPE,
                                             new VideoPopupMenuOnItemClickHandler(getContext()));
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    mRecyclerViewTrendVideo.setLayoutManager(layoutManager);
    mRecyclerViewTrendVideo.addItemDecoration(dividerItemDecoration);
    mRecyclerViewTrendVideo.setAdapterWithProgress(mVideoListAdapter);

    mVideoListAdapter.setMore(R.layout.rv_more, this);
    mVideoListAdapter.setError(R.layout.rv_error, new RecyclerArrayAdapter.OnErrorListener()
    {
      @Override
      public void onErrorShow()
      {
        mVideoListAdapter.resumeMore();
      }

      @Override
      public void onErrorClick()
      {
        mVideoListAdapter.resumeMore();
      }
    });

    mVideoListAdapter.clear();
    mVideoListAdapter.pauseMore();
  }

  @Override
  public void onStart()
  {
    super.onStart();
    mGetTrendVideoTask.getTrendVideoAsync(mGetTrendVideoCallback);
  }

  @Override
  public void onLoadMore()
  {
    mGetTrendVideoTask.getNextTrendVideoAsync();
  }
}
