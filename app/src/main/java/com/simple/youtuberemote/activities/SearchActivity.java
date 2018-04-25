package com.simple.youtuberemote.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ImageView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.YoutubeApiHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends AppCompatActivity
    implements RecyclerArrayAdapter.OnLoadMoreListener
{

  public static final  int    SEARCH_REQUEST_CODE = 10;
  private static final String TAG                 = SearchActivity.class.getSimpleName();

  @BindView (R.id.iv_action_back)
  ImageView        mActionBack;
  @BindView (R.id.searchView)
  SearchView       mSearchView;
  @BindView (R.id.rv_search_results)
  EasyRecyclerView mResultVideoList;

  private VideoListAdapter mResultVideoListAdapter;

  private Handler mHandler;
  private String  mQuery;
  private Boolean mIsNewSearch = false;
  private List<VideoItem> mSearchResults;

  @Override
  public void onLoadMore()
  {
    Log.d(TAG, "Load more search results...");
    searchOnYoutube(mQuery);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ButterKnife.bind(this);

    mSearchView.setIconifiedByDefault(false);
    mSearchView.setQueryHint("Search YouTube");
    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
    {
      @Override
      public boolean onQueryTextSubmit(String query)
      {
        Log.d(TAG, "Start new searching...");

        mIsNewSearch = true;
        mSearchView.clearFocus();

        mQuery = query;
        searchOnYoutube(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText)
      {
        return false;
      }
    });

    initResultVideoListView();

    mHandler = new Handler();
  }

  @OnClick (R.id.iv_action_back)
  void back()
  {
    boolean result = true;
    if (!result) {
      setResult(Activity.RESULT_CANCELED);
    }
    else {
      Intent resultIntent = new Intent();
      setResult(Activity.RESULT_OK, resultIntent);
    }
    finish();
  }

  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(this);


    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mResultVideoList.setLayoutManager(layoutManager);
    mResultVideoList.addItemDecoration(new DividerItemDecoration(this,
                                                                 DividerItemDecoration.VERTICAL));

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

  private void searchOnYoutube(final String query)
  {
    new Thread()
    {
      public void run()
      {
        Log.d(TAG, "Requesting...");
        YoutubeApiHelper yc = new YoutubeApiHelper(SearchActivity.this);
        mSearchResults = yc.search(query);
        mHandler.post(new Runnable()
        {
          public void run()
          {
            Log.d(TAG, "Response: " + mSearchResults);
            if (mSearchResults != null) {
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
        });
      }
    }.start();
  }

}

