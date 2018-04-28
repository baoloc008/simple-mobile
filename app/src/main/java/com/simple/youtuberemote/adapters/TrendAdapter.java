package com.simple.youtuberemote.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.simple.youtuberemote.R;
import com.simple.youtuberemote.models.VideoItem;

import java.util.List;


/**
 * Created by loc on 15/04/2018.
 */

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.ViewHolder>
{
  private Context         context;
  private List<VideoItem> videoList;

  public TrendAdapter(Context context, List<VideoItem> videoList)
  {
    this.context = context;
    this.videoList = videoList;
  }

  @Override
  public TrendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    LayoutInflater layoutInflater
        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.video_item_full, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(TrendAdapter.ViewHolder holder, int position)
  {
    final VideoItem videoItem = videoList.get(position);
    holder.txtvTitle.setText(videoItem.getTitle());
    holder.txtvSubTitle.setText(videoItem.getChannelTitle());
    Glide.with(context)
         .load(videoItem.getThumbnailUrl())
         .into(holder.imgvThumbnail);
    holder.imgvThumbnail.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        Toast.makeText(context, String.valueOf(videoItem.getVideoId()), Toast.LENGTH_SHORT).show();
//        Bundle bundle = new Bundle();
//        bundle.putString("videoId", videoItem.getVideoId());
//        Intent intent = new Intent(context, PlayVideo.class);
//        intent.putExtra("params", bundle);
//        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount()
  {
    return videoList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder
  {
    ImageView imgvThumbnail;
    TextView  txtvTitle, txtvSubTitle;

    public ViewHolder(View itemView)
    {
      super(itemView);
      imgvThumbnail = itemView.findViewById(R.id.video_item_full_iv_thumbnail);
      txtvTitle = itemView.findViewById(R.id.video_item_full_tv_title);
      txtvSubTitle = itemView.findViewById(R.id.video_item_full_tv_statistics);
    }
  }
}
