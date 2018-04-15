package com.simple.youtuberemote.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.adapters.ViewPagerHomeAdapter;

public class RemoteControlActivity extends AppCompatActivity {
  private TabLayout tabLayoutHome;
  private ViewPager viewPagerHome;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_control);
    mapComponent();
    ViewPagerHomeAdapter viewPagerHomeAdapter = new ViewPagerHomeAdapter(getSupportFragmentManager(), this);
    viewPagerHome.setAdapter(viewPagerHomeAdapter);
    tabLayoutHome.setupWithViewPager(viewPagerHome);
  }
  private void mapComponent() {
    tabLayoutHome = findViewById(R.id.tabLayoutHome);
    viewPagerHome = findViewById(R.id.viewPagerHome);
  }
}
