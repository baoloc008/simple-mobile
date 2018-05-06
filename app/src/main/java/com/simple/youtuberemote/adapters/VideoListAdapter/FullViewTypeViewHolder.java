package com.simple.youtuberemote.adapters.VideoListAdapter;

import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Utils;


public class FullViewTypeViewHolder extends BaseViewHolder<VideoItem>
{

  VideoItem   mVideo;
  ImageView   mThumbnail;
  TextView    mTitle;
  TextView    mStatistics;
  TextView    mDuration;
  ImageButton mMenuButton;

  VideoListAdapter.OnItemPopupMenuClickListener mListener;

  public FullViewTypeViewHolder(ViewGroup parent,
                                VideoListAdapter.OnItemPopupMenuClickListener listener)
  {
    super(parent, R.layout.video_item_full);

    mListener = listener;

    mThumbnail = $(R.id.video_item_full_iv_thumbnail);
    mTitle = $(R.id.video_item_full_tv_title);
    mStatistics = $(R.id.video_item_full_tv_statistics);
    mDuration = $(R.id.video_item_full_tv_duration);
    mMenuButton = $(R.id.video_item_full_tv_menu);

    mMenuButton.setOnClickListener(new View.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        showPopupMenu(mMenuButton);
      }
    });
  }

  @Override
  public void setData(VideoItem video)
  {
    mVideo = video;
    Glide.with(getContext()).load(video.getThumbnailUrl()).into(mThumbnail);
    mTitle.setText(video.getTitle());
    mStatistics.setText(getContext().getString(R.string.video_item_statistics,
                                               video.getChannelTitle(),
                                               Utils.prettyViewCount(video.getViewCount())));
    mDuration.setText(Utils.formatDuration(video.getDuration()));
  }

  private void showPopupMenu(View view)
  {
    PopupMenu    popup    = new PopupMenu(view.getContext(), view);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.video_item, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
    {
      @Override
      public boolean onMenuItemClick(MenuItem item)
      {
        switch (item.getItemId()) {
          case R.id.video_item_action_add_to_playlist:
            mListener.onAddToPlaylist(mVideo);
            return true;
          case R.id.video_item_action_play:
            mListener.onPlay(mVideo);
            return true;
          default:
            return false;
        }
      }
    });
    popup.show();
  }
}
