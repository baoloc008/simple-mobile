package com.simple.youtuberemote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.simple.youtuberemote.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
{

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
  }

  @OnClick (R.id.btn_television)
  void startTelevisionActivity()
  {
    Intent intent = new Intent(this, TelevisionActivity.class);
    startActivity(intent);
  }

  @OnClick (R.id.btn_remote_control)
  void startRemoteControlActivity()
  {
    Intent intent = new Intent(this, RemoteControlActivity.class);
    startActivity(intent);
  }

}
