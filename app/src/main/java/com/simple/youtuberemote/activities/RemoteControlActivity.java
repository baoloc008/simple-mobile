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


public class RemoteControlActivity extends AppCompatActivity
{
  private TabLayout tabLayoutHome;
  private ViewPager viewPagerHome;
  private Client    mClient;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_control);
    mapComponent();
    ViewPagerHomeAdapter viewPagerHomeAdapter
        = new ViewPagerHomeAdapter(getSupportFragmentManager(), this);
    viewPagerHome.setAdapter(viewPagerHomeAdapter);
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

  private void mapComponent()
  {
    tabLayoutHome = findViewById(R.id.tabLayoutHome);
    viewPagerHome = findViewById(R.id.viewPagerHome);
  }
}
