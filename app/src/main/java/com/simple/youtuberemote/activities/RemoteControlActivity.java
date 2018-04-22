package com.simple.youtuberemote.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.adapters.ViewPagerHomeAdapter;
import com.simple.youtuberemote.networks.Client;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RemoteControlActivity extends AppCompatActivity
{
  @BindView (R.id.tabLayoutHome)
  TabLayout tabLayoutHome;
  @BindView (R.id.viewPagerHome)
  ViewPager viewPagerHome;
  Client mClient;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_control);
    ButterKnife.bind(this);
    ViewPagerHomeAdapter viewPagerHomeAdapter
        = new ViewPagerHomeAdapter(getSupportFragmentManager(), this);
    // viewPagerHome = findViewById(R.id.viewPagerHome);
    viewPagerHome.setAdapter(viewPagerHomeAdapter);
    // tabLayoutHome = findViewById(R.id.tabLayoutHome);
    tabLayoutHome.setupWithViewPager(viewPagerHome);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    mClient = new Client(this)
    {
      @Override
      public void onPlaylistChange(ArrayList<String> playList, String currentVideo)
      {
        Log.d("PlayList Change", playList.toString());
      }
    };
  }

  @Override
  protected void onStop()
  {
    mClient.close();
    super.onStop();
  }
}
