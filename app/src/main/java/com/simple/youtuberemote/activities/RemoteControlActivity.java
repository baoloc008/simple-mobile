package com.simple.youtuberemote.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.simple.youtuberemote.R;
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

  public static Client mClient;
  private int[] tabIcons = {
      R.drawable.ic_appbar_home,
      R.drawable.ic_appbar_trending,
      R.drawable.ic_appbar_play_list
  };

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_control);

    ButterKnife.bind(this);

    ViewPagerHomeAdapter viewPagerHomeAdapter
        = new ViewPagerHomeAdapter(getSupportFragmentManager(), this);
    viewPagerHome.setAdapter(viewPagerHomeAdapter);

    tabLayoutHome.setupWithViewPager(viewPagerHome);
    setupTabIcons();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    mClient = new Client(this);
  }

  @Override
  protected void onStop()
  {
    mClient.close();
    super.onStop();
  }

  private void setupTabIcons()
  {
    tabLayoutHome.getTabAt(0).setIcon(tabIcons[0]);
    tabLayoutHome.getTabAt(1).setIcon(tabIcons[1]);
    tabLayoutHome.getTabAt(2).setIcon(tabIcons[2]);
  }
}
