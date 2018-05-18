package com.simple.youtuberemote.adapters.VideoListAdapter;

import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Utils;


public class CompactViewTypeViewHolder extends BaseViewHolder<VideoItem>
{

  VideoItem mVideo;

  RelativeLayout mRelativeLayout;
  ImageView      mThumbnail;
  TextView       mTitle;
  TextView       mChannelTitle;
  TextView       mDuration;
  TextView       mStatistics;
  ImageButton    mMenuButton;
  int            mPopupType;

  VideoListAdapter.OnItemPopupMenuClickListener mListener;

  public CompactViewTypeViewHolder(ViewGroup parent,
                                   VideoListAdapter.OnItemPopupMenuClickListener listener,
                                   int popupType)
  {
    super(parent, R.layout.video_item_compact);

    mListener = listener;
    mPopupType = popupType;

    mRelativeLayout = $(R.id.video_item_compact_rl);
    mThumbnail = $(R.id.video_item_compact_iv_thumbnail);
    mTitle = $(R.id.video_item_compact_tv_title);
    mChannelTitle = $(R.id.video_item_compact_tv_channel_title);
    mMenuButton = $(R.id.video_item_compact_tv_menu);
    mDuration = $(R.id.video_item_compact_tv_duration);
    mStatistics = $(R.id.video_item_compact_tv_view_count);

    mMenuButton.setOnClickListener(new View.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {

        switch (mPopupType) {
          case VideoListAdapter.POPUP_MEMNU_NORMAL: {
            showPopupMenuNormal(mMenuButton);
            break;
          }
          case VideoListAdapter.POPUP_MEMNU_PLAYLIST: {
            showPopupMenuPlaylist(mMenuButton);
            break;
          }
          default:
            break;
        }
      }
    });
  }

  @Override
  public void setData(VideoItem video)
  {
    mVideo = video;
    Glide.with(getContext())
         .load(video.getThumbnailUrl())
         .asBitmap()
         .format(DecodeFormat.PREFER_ARGB_8888)
         .into(mThumbnail);
    mTitle.setText(video.getTitle());
    mChannelTitle.setText(video.getChannelTitle());
    mStatistics.setText(getContext().getString(R.string.video_item_statistics,
                                               Utils.prettyViewCount(video.getViewCount())));
    mDuration.setText(Utils.formatDuration(video.getDuration()));

    if (video.isPlaying()) {
      mRelativeLayout.setBackgroundColor(Color.rgb(193, 193, 193));
    }
    else {
      mRelativeLayout.setBackgroundColor(Color.rgb(255, 255, 255));
    }
  }

  private void showPopupMenuNormal(View view)
  {
    PopupMenu    popup    = new PopupMenu(view.getContext(), view);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.video_item_normal, popup.getMenu());
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

  private void showPopupMenuPlaylist(View view)
  {
    PopupMenu    popup    = new PopupMenu(view.getContext(), view);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.video_item_playlist, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
    {
      @Override
      public boolean onMenuItemClick(MenuItem item)
      {
        switch (item.getItemId()) {
          case R.id.video_item_action_play_in_playlist:
            mListener.onPlay(mVideo);
            return true;

          case R.id.video_item_action_delete:
            mListener.onDelete(mVideo);
            return true;
          default:
            return false;
        }
      }
    });
    popup.show();
  }
}
