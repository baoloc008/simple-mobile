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
  @BindView (R.id.youtube_view)
  YouTubePlayerView mYoutubeView;
  @BindView (R.id.empty_tv)
  TextView          emptyTextView;
  @BindView (R.id.client_count_tv)
  TextView clientCountTextView;
  private YouTubePlayer mYouTubePlayer;
  private Server        mServer;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_television);

    ButterKnife.bind(this);

    mYoutubeView.initialize(DEVELOPER_KEY, this);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    mServer = new Server()
    {
      @Override
      public void onClientChange(int count)
      {
        clientCountTextView.setText(String.valueOf(count));
      }

      @Override
      public void onPlayListFilled()
      {
        emptyTextView.setVisibility(View.GONE);
        mYoutubeView.setVisibility(View.VISIBLE);
        mYouTubePlayer.setFullscreen(true);
      }

      @Override
      public void onPlayListEmpty()
      {
        emptyTextView.setVisibility(View.VISIBLE);
        mYoutubeView.setVisibility(View.GONE);
      }

      @Override
      public void onVideoChange(String id)
      {
        mYouTubePlayer.loadVideo(id);
      }

      @Override
      public void onPause()
      {
        mYouTubePlayer.pause();
      }

      @Override
      public void onPlay()
      {
        mYouTubePlayer.play();
      }
    };
    mServer.start();
  }

  @Override
  protected void onStop()
  {
    mServer.close();
    super.onStop();
  }

  @Override
  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                      boolean wasRestored)
  {

    if (!wasRestored) {
      mYouTubePlayer = player;
      mYouTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION |
                                               YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
    }
  }

  @Override
  protected YouTubePlayer.Provider getYouTubePlayerProvider()
  {
    return (YouTubePlayerView) findViewById(R.id.youtube_view);
  }
}
