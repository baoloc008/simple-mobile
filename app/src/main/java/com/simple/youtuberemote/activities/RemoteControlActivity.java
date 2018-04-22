package com.simple.youtuberemote.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
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
  private int[] tabIcons = {
      R.drawable.ic_home_white_24dp,
      R.drawable.ic_whatshot_white_24dp,
      R.drawable.ic_featured_play_list_white_24dp
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

  private void setupTabIcons()
  {
    final int WHITE           = ContextCompat.getColor(this, R.color.white);
    final int BLACK_SECONDARY = ContextCompat.getColor(this, R.color.blackSecondary);

    tabLayoutHome.getTabAt(0).setIcon(tabIcons[0]);
    tabLayoutHome.getTabAt(1).setIcon(tabIcons[1]);
    tabLayoutHome.getTabAt(2).setIcon(tabIcons[2]);

    tabLayoutHome.getTabAt(0).getIcon().setColorFilter(WHITE, PorterDuff.Mode.SRC_IN);
    tabLayoutHome.getTabAt(1).getIcon().setColorFilter(BLACK_SECONDARY, PorterDuff.Mode.SRC_IN);
    tabLayoutHome.getTabAt(2).getIcon().setColorFilter(BLACK_SECONDARY, PorterDuff.Mode.SRC_IN);

    tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
    {
      @Override
      public void onTabSelected(TabLayout.Tab tab)
      {
        tab.getIcon().setColorFilter(WHITE, PorterDuff.Mode.SRC_IN);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab)
      {
        tab.getIcon().setColorFilter(BLACK_SECONDARY, PorterDuff.Mode.SRC_IN);
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab)
      {

      }
    });
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
