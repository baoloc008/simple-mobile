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

  private int                          mViewType;
  private OnItemPopupMenuClickListener mListener;

  public VideoListAdapter(Context context,
                          int viewType,
                          OnItemPopupMenuClickListener listener)
  {
    super(context);

    mViewType = viewType;
    mListener = listener;
  }

  @Override
  public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
  {
    switch (mViewType) {
      case FULL_VIEW_TYPE:
        return new FullViewTypeViewHolder(parent, mListener);
      case COMPACT_VIEW_TYPE:
        return new CompactViewTypeViewHolder(parent, mListener);
      default:
        return new FullViewTypeViewHolder(parent, mListener);
    }
  }

  public interface OnItemPopupMenuClickListener
  {

    void onAddToPlayist(VideoItem video);

    void onPlay(VideoItem video);
  }

}
