package com.simple.youtuberemote.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.networks.Client;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
{

  private ProgressDialog mProgressDialog;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setMessage("Đang kết nối...");
    mProgressDialog.setTitle("Kết nối đến TV");
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
    mProgressDialog.show();
    Client client = Client.getInstance(this);
    client.findServer(new Runnable()
    {
      @Override
      public void run()
      {
        Toast.makeText(getApplicationContext(), "Đã kết nối", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        Intent intent = new Intent(MainActivity.this, RemoteControlActivity.class);
        startActivity(intent);
      }
    });
  }
}
