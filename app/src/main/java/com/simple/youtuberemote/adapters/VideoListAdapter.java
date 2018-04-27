package com.simple.youtuberemote.adapters;

import android.content.Context;
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
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.utils.Utils;


public class VideoListAdapter extends RecyclerArrayAdapter<VideoItem>
{

  private PopupMenuOnItemClickListener mPopupMenuOnItemClickListener;

  public VideoListAdapter(Context context,
                          PopupMenuOnItemClickListener popupMenuOnItemClickListener)
  {
    super(context);
    mPopupMenuOnItemClickListener = popupMenuOnItemClickListener;
  }

  @Override
  public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
  {
    return new VideoViewHolder(parent);
  }

  public interface PopupMenuOnItemClickListener
  {

    void onAddToPlayist(VideoItem video);

    void onPlay(VideoItem video);
  }

  public class VideoViewHolder extends BaseViewHolder<VideoItem>
  {

    VideoItem   mVideo;
    ImageView   mThumbnail;
    TextView    mTitle;
    TextView    mStatistics;
    TextView    mDuration;
    ImageButton mMenuButton;

    public VideoViewHolder(ViewGroup parent)
    {
      super(parent, R.layout.video_item);

      mThumbnail = $(R.id.iv_video_thumbnail);
      mTitle = $(R.id.tv_video_title);
      mStatistics = $(R.id.tv_video_statistics);
      mDuration = $(R.id.tv_video_duration);
      mMenuButton = $(R.id.ib_menu);

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
                                                 Utils.formatDecimal(video.getViewCount())));
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
              mPopupMenuOnItemClickListener.onAddToPlayist(mVideo);
              return true;
            case R.id.video_item_action_play:
              mPopupMenuOnItemClickListener.onPlay(mVideo);
              return true;
            default:
              return false;
          }
        }
      });
      popup.show();
    }

  }

}
