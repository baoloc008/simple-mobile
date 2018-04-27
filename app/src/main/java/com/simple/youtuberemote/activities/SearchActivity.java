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
import com.simple.youtuberemote.adapters.VideoListAdapter;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.YoutubeApiHelper;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

import java.util.Arrays;
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
  private List<String> mSuggestions = Arrays.asList(
      "Bauru", "Sao Paulo", "Rio de Janeiro",
      "Bahia", "Mato Grosso", "Minas Gerais",
      "Tocantins", "Rio Grande do Sul");

  private YoutubeApiHelper.SearchTask mSearchTask;
  private List<VideoItem>             mSearchResults;
  private String                      mQuery;
  private Boolean mIsNewSearch = false;

  private YoutubeApiHelper.SearchTask.Callback mSearchTaskCallback
      = new YoutubeApiHelper.SearchTask.Callback()
  {

    @Override
    public void onSearchComplete(boolean ok, List<String> result)
    {
      if (ok) {
        mSearchResults = YoutubeApiHelper.requestVideosInfoById(result);
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
    Log.d(TAG, "Load more search results...");
    mSearchTask.searchNext();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ButterKnife.bind(this);

    initSearchView();
    initResultVideoListView();

    mSearchTask = new YoutubeApiHelper.SearchTask();
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
        if (mSuggestions.size() > 0) {
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
        mSearchView.clearFocus();

        mSearchTask.search(query, mSearchTaskCallback);
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

  private void populateSuggestionAdapter(String query)
  {
    String[] columns = {
        BaseColumns._ID,
        SearchManager.SUGGEST_COLUMN_TEXT_1
    };

    MatrixCursor c = new MatrixCursor(columns);
    for (int i = 0; i < mSuggestions.size(); i++) {
      if (mSuggestions.get(i).toLowerCase().startsWith(query.toLowerCase())) {
        c.addRow(new Object[]{ i, mSuggestions.get(i) });
      }
    }
    mSuggestionAdapter.changeCursor(c);
  }

  private void initResultVideoListView()
  {
    mResultVideoListAdapter = new VideoListAdapter(this,
                                                   new VideoPopupMenuOnItemClickHandler(this));

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

}

