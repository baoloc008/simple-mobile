package com.simple.youtuberemote.adapters.VideoListAdapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.simple.youtuberemote.models.VideoItem;


public class VideoListAdapter extends RecyclerArrayAdapter<VideoItem>
{

  public static final int FULL_VIEW_TYPE    = 0;
  public static final int COMPACT_VIEW_TYPE = 1;

  public static final int POPUP_MEMNU_NORMAL = 0;
  public static final int POPUP_MEMNU_PLAYLIST = 1;

  private int                          mViewType;
  private int                          mPopupType;
  private OnItemPopupMenuClickListener mListener;

  public VideoListAdapter(Context context,
                          int viewType,
                          int popupType,
                          OnItemPopupMenuClickListener listener)
  {
    super(context);

    mViewType = viewType;
    mListener = listener;
    mPopupType = popupType;
  }

  @Override
  public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
  {
    switch (mViewType) {
      case FULL_VIEW_TYPE:
        return new FullViewTypeViewHolder(parent, mListener);
      case COMPACT_VIEW_TYPE:
        return new CompactViewTypeViewHolder(parent, mListener, mPopupType);
      default:
        return new FullViewTypeViewHolder(parent, mListener);
    }
  }

  public interface OnItemPopupMenuClickListener
  {

    void onAddToPlaylist(VideoItem video);

    void onPlay(VideoItem video);

    void onDelete(VideoItem video);
  }
}
