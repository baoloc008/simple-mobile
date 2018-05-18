package com.simple.youtuberemote.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.activities.SearchActivity;
import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.YoutubeApi.FetchVideoDetailTask;
import com.simple.youtuberemote.networks.YoutubeApi.SearchYoutubeTask;
import com.simple.youtuberemote.networks.YoutubeApi.YoutubeApiHelper;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by loc on 15/04/2018.
 */

public class HomeFragment extends Fragment
    implements RecyclerArrayAdapter.OnLoadMoreListener
{
  @BindView (R.id.tv_empty_message)
  TextView mEmptyMessage;

  @BindView (R.id.fab_search)
  FloatingActionButton mSearchButton;

  @BindView (R.id.rv_home_video)
  EasyRecyclerView mResultVideoList;

  private VideoListAdapter mResultVideoListAdapter;
  private List<VideoItem>  mSearchResults;
  private Boolean mIsNewSearch = false;

  private SearchYoutubeTask    mSearchTask = YoutubeApiHelper.searchYoutube();
  private FetchVideoDetailTask mFetchTask  = YoutubeApiHelper.fetchVideoDetail();

  private SearchYoutubeTask.Callback mSearchTaskCallback
      = new SearchYoutubeTask.Callback()
  {

    @Override
    public void onSearchComplete(boolean ok, List<String> result)
    {
      if (ok) {
        mSearchResults = mFetchTask.fetch(result);
        if (mIsNewSearch) {
          mResultVideoListAdapter.clear();
          mIsNewSearch = false;
        }
        mResultVideoListAdapter.addAll(mSearchResults);
      }
      else {
        mResultVideoListAdapter.pauseMore();
        mResultVideoList.showError();
      }
    }
  };

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);
    initResultVideoListView();

    return view;
  }

  @Override
  public void onStart()
  {
    super.onStart();
    mSearchTask.searchAsync("karaoke", mSearchTaskCallback);
  }

  @Override
  public void onLoadMore()
  {
    mSearchTask.searchNextAsync();
  }

  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(getContext(), VideoListAdapter.COMPACT_VIEW_TYPE,
                                                   VideoListAdapter.POPUP_MEMNU_NORMAL,
                                                   new VideoPopupMenuOnItemClickHandler(getContext()));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                                                                            DividerItemDecoration.VERTICAL);
    mResultVideoList.setLayoutManager(layoutManager);
    mResultVideoList.addItemDecoration(dividerItemDecoration);
    mResultVideoList.setAdapterWithProgress(mResultVideoListAdapter);

    mResultVideoListAdapter.setMore(R.layout.rv_more, this);
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

  @OnClick (R.id.fab_search)
  void searchVideo()
  {
    Intent intent = new Intent(getContext(), SearchActivity.class);
    startActivityForResult(intent, SearchActivity.SEARCH_REQUEST_CODE);
  }
}
