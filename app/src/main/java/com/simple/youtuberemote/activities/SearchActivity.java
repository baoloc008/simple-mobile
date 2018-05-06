package com.simple.youtuberemote.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ImageView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.VideoListAdapter.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.SuggestionApi.SuggestionApiHelper;
import com.simple.youtuberemote.networks.YoutubeApi.FetchVideoDetailTask;
import com.simple.youtuberemote.networks.YoutubeApi.SearchYoutubeTask;
import com.simple.youtuberemote.networks.YoutubeApi.YoutubeApiHelper;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

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

  private CursorAdapter mSuggestionAdapter;
  private List<String>  mSuggestions;

  private List<VideoItem> mSearchResults;
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
        Log.d(TAG, "Search Response: " + mSearchResults);
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

  @Override
  public void onLoadMore()
  {
    Log.d(TAG, "Load more searchAsync results...");
    mSearchTask.searchNextAsync();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ButterKnife.bind(this);

    initSearchView();
    initResultVideoListView();
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

  private void initSearchView()
  {
    mSearchView.setIconifiedByDefault(false);
    mSearchView.setQueryHint("Tìm kiếm YouTube");

    String[] from = new String[]{ SearchManager.SUGGEST_COLUMN_TEXT_1 };
    int[]    to   = new int[]{ android.R.id.text1 };
    mSuggestionAdapter = new SimpleCursorAdapter(this,
                                                 android.R.layout.simple_list_item_1,
                                                 null,
                                                 from, to,
                                                 0);
    mSearchView.setSuggestionsAdapter(mSuggestionAdapter);
    mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
    {
      @Override
      public boolean onSuggestionSelect(int position)
      {
        return false;
      }

      @Override
      public boolean onSuggestionClick(int position)
      {
        if (mSuggestions != null && mSuggestions.size() > 0) {
          mSearchView.setQuery(mSuggestions.get(position), true);
          mSearchView.clearFocus();
          return true;
        }
        return false;
      }
    });

    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
    {
      @Override
      public boolean onQueryTextSubmit(String query)
      {
        Log.d(TAG, "Start new searching...");

        mIsNewSearch = true;
        mSearchTask.searchAsync(query, mSearchTaskCallback);

        mSearchView.clearFocus();
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText)
      {
        populateSuggestionAdapter(newText);
        return false;
      }
    });
  }

  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(this, VideoListAdapter.COMPACT_VIEW_TYPE,
                                                   new VideoPopupMenuOnItemClickHandler(this));

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
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

  private void populateSuggestionAdapter(String query)
  {
    SuggestionApiHelper.fetchAsync(query, new SuggestionApiHelper.Callback()
    {
      @Override
      public void onFetchComplete(boolean ok, List<String> suggestions)
      {
        Log.d(TAG, "SearchActivity suggestion");
        if (!ok || suggestions.size() == 0) {
          Log.d(TAG, "false");
          return;
        }
        String[] columns = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1
        };
        Log.d(TAG, suggestions.toString());
        mSuggestions = suggestions;
        MatrixCursor c = new MatrixCursor(columns);

        for (int i = 0; i < mSuggestions.size(); i++) {
          c.addRow(new Object[]{ i, mSuggestions.get(i) });
        }
        mSuggestionAdapter.changeCursor(c);
      }
    });
  }
}

