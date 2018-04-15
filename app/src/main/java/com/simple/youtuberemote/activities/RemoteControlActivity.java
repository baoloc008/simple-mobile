package com.simple.youtuberemote.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.simple.youtuberemote.R;


public class RemoteControlActivity extends AppCompatActivity {
  private TabLayout tabLayoutHome;
  private ViewPager viewPagerHome;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_control);
    mapComponent();
  }
  private void mapComponent() {
    tabLayoutHome = findViewById(R.id.tabLayoutHome);
    viewPagerHome = findViewById(R.id.viewPagerHome);
  }
}
