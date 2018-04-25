package com.simple.youtuberemote.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;

import com.simple.youtuberemote.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends AppCompatActivity
{

  public static final int SEARCH_REQUEST_CODE = 10;

  @BindView (R.id.iv_action_back)
  ImageView    mActionBack;
  @BindView (R.id.searchView)
  SearchView   mSearchView;
  @BindView (R.id.rv_search_results)
  RecyclerView mSearchVideoList;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ButterKnife.bind(this);

    mSearchView.setIconifiedByDefault(false);
    mSearchView.setQueryHint("Search YouTube");

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

}
