package com.simple.youtuberemote.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.networks.Server;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TelevisionActivity extends YouTubeFailureRecoveryActivity
{

  private static final String DEVELOPER_KEY    = "AIzaSyDiUkq0Jx96EW-D-zuqR45KYIvDmAnPn1s";
  private static final String DEFAULT_VIDEO_ID = "wKJ9KzGQq0w";

  private Server mServer;

  @BindView (R.id.youtube_view)
  YouTubePlayerView mYoutubeView;
  @BindView(R.id.ip_tv) TextView ipTextView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_television);

    ButterKnife.bind(this);

    mServer = new Server()
    {
      @Override
      public void onGetIp(String ip)
      {
        ipTextView.setText(ip);
      }

      @Override
      public void onPlayListFilled()
      {
        ipTextView.setVisibility(View.GONE);
        mYoutubeView.setVisibility(View.VISIBLE);
      }

      @Override
      public void onPlayListEmpty()
      {
        ipTextView.setVisibility(View.VISIBLE);
        mYoutubeView.setVisibility(View.GONE);
      }

      @Override
      public void onVideoChange(String id)
      {

      }

      @Override
      public void onPause()
      {

      }

      @Override
      public void onPlay()
      {

      }
    };
    mServer.start(this);

    mYoutubeView.initialize(DEVELOPER_KEY, this);
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                      boolean wasRestored)
  {
    if (!wasRestored) {
      player.cueVideo(DEFAULT_VIDEO_ID);
    }
  }

  @Override
  protected YouTubePlayer.Provider getYouTubePlayerProvider()
  {
    return (YouTubePlayerView) findViewById(R.id.youtube_view);
  }

}
