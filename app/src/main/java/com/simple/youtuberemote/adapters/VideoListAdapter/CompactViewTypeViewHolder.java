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
import com.bumptech.glide.load.DecodeFormat;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Utils;


public class CompactViewTypeViewHolder extends BaseViewHolder<VideoItem>
{

  VideoItem mVideo;

  ImageView   mThumbnail;
  TextView    mTitle;
  TextView    mChannelTitle;
  TextView    mDuration;
  TextView    mStatistics;
  ImageButton mMenuButton;

  VideoListAdapter.OnItemPopupMenuClickListener mListener;

  public CompactViewTypeViewHolder(ViewGroup parent,
                                   VideoListAdapter.OnItemPopupMenuClickListener listener)
  {
    super(parent, R.layout.video_item_compact);

    mListener = listener;

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
        showPopupMenu(mMenuButton);
      }
    });
  }

  @Override
  public void setData(VideoItem video)
  {
    mVideo = video;
    Glide.with(getContext()).load(video.getThumbnailUrl()).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(mThumbnail);
    mTitle.setText(video.getTitle());
    mChannelTitle.setText(video.getChannelTitle());
    mStatistics.setText(getContext().getString(R.string.video_item_statistics,
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
            mListener.onAddToPlayist(mVideo);
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
