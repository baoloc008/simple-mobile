package com.simple.youtuberemote.activities;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.simple.youtuberemote.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TelevisionActivity extends YouTubeFailureRecoveryActivity
{

  private static final String DEVELOPER_KEY    = "AIzaSyDiUkq0Jx96EW-D-zuqR45KYIvDmAnPn1s";
  private static final String DEFAULT_VIDEO_ID = "wKJ9KzGQq0w";

  @BindView (R.id.youtube_view)
  YouTubePlayerView mYoutubeView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_television);

    ButterKnife.bind(this);
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
