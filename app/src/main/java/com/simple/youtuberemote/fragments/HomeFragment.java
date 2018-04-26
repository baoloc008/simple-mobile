package com.simple.youtuberemote.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.activities.RemoteControlActivity;
import com.simple.youtuberemote.activities.SearchActivity;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.adapters.VideoListAdapter;
import com.simple.youtuberemote.models.API.searchvideos.Item;
import com.simple.youtuberemote.models.API.searchvideos.SearchVideos;
import com.simple.youtuberemote.models.API.videodetail.VideoDetail;
import com.simple.youtuberemote.models.VideoItem;
import com.simple.youtuberemote.networks.retrofit.APIUtils;
import com.simple.youtuberemote.networks.retrofit.DataClient;
import com.simple.youtuberemote.utils.VideoPopupMenuOnItemClickHandler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by loc on 15/04/2018.
 */

public class HomeFragment extends Fragment
    implements HomeAdapter.RecyclerViewHomeAdapterOnclickListener
{

  @BindView (R.id.rv_home_videos)
  RecyclerView mVideoList;

  @BindView (R.id.tv_empty_message)
  TextView mEmptyMessage;

  @BindView (R.id.fab_search)
  FloatingActionButton mSearchButton;

  private VideoListAdapter    homeAdapter;
  private List<VideoItem>     videoList;
  private DataClient          dataClient;
  private AlertDialog.Builder mBuilder;
  private AlertDialog         mAlertDialog;
  private TextView            txtvClickPlay, txtvClickAddPlaylist;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    ButterKnife.bind(this, view);

    videoList = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    homeAdapter = new VideoListAdapter(getContext(),
                                       new VideoPopupMenuOnItemClickHandler(getContext()));
    mVideoList.setLayoutManager(layoutManager);
    mVideoList.addItemDecoration(new DividerItemDecoration(getContext(),
                                                           DividerItemDecoration.VERTICAL));
    mVideoList.setAdapter(homeAdapter);

    dataClient = APIUtils.getData();
    dataClient.getSearchVideos("phim songoku").enqueue(new Callback<SearchVideos>()
    {
      @Override
      public void onResponse(@NonNull Call<SearchVideos> call,
                             @NonNull Response<SearchVideos> response)
      {
        if (response.isSuccessful()) {
          SearchVideos searchVideos = response.body();
          if (searchVideos != null) {
            List<Item> videoAPIList = searchVideos.getItems();
            for (final Item item : videoAPIList) {
              final VideoItem videoItem = new VideoItem();
              videoItem.setThumbnailUrl(item.getSnippet().getThumbnails().getMedium().getUrl());
              videoItem.setTitle(item.getSnippet().getTitle());
              videoItem.setVideoId(item.getId().getVideoId());
              dataClient.getVideoDetail(item.getId().getVideoId())
                        .enqueue(new Callback<VideoDetail>()
                        {
                          @Override
                          public void onResponse(@NonNull Call<VideoDetail> call,
                                                 @NonNull Response<VideoDetail> response)
                          {
                            if (response.isSuccessful()) {
                              VideoDetail videoDetail = response.body();
                              String duration = videoDetail.getItems()
                                                           .get(0)
                                                           .getContentDetails()
                                                           .getDuration();
                              videoItem.setDuration(duration);
                              videoItem.setViewCount(new BigInteger(videoDetail.getItems()
                                                                               .get(0)
                                                                               .getStatistics()
                                                                               .getViewCount()));
                              videoItem.setChannelTitle(item.getSnippet().getChannelTitle());
                              homeAdapter.add(videoItem);
                            }
                            else {
                              Toast.makeText(getActivity(),
                                             String.valueOf("Error2:" + response.code()),
                                             Toast.LENGTH_SHORT).show();
                            }
                          }

                          @Override
                          public void onFailure(@NonNull Call<VideoDetail> call,
                                                @NonNull Throwable t)
                          {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT)
                                 .show();
                          }
                        });
              videoList.add(videoItem);

//              homeAdapter.notifyDataSetChanged();
            }
          }
        }
        else {
          Toast.makeText(getActivity(),
                         String.valueOf("Error 1:" + response.code()),
                         Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(@NonNull Call<SearchVideos> call, @NonNull Throwable t)
      {
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

    mBuilder = new AlertDialog.Builder(getActivity());
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_video_option, null);
    mBuilder.setView(dialogView);
    txtvClickPlay = dialogView.findViewById(R.id.txtvPlay);
    txtvClickAddPlaylist = dialogView.findViewById(R.id.txtvAddToPlaylist);

    mAlertDialog = mBuilder.create();
    return view;
  }

  @Override
  public void onItemClick(View v, int position)
  {
    final VideoItem videoItem = videoList.get(position);
    txtvClickPlay.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mAlertDialog.dismiss();
        try {
          RemoteControlActivity.mClient.playVideo(videoItem.getVideoId());
        }
        catch (Exception e) {
          Toast.makeText(getActivity(), "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
        }
      }
    });
    txtvClickAddPlaylist.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        mAlertDialog.dismiss();
        try {
          RemoteControlActivity.mClient.addVideo(videoItem.getVideoId());
        }
        catch (Exception e) {
          Toast.makeText(getActivity(), "Chưa kết nối đến TV", Toast.LENGTH_SHORT).show();
        }
      }
    });

    mAlertDialog.show();
  }

  @OnClick (R.id.fab_search)
  void searchVideo()
  {
    Intent intent = new Intent(getContext(), SearchActivity.class);
    startActivityForResult(intent, SearchActivity.SEARCH_REQUEST_CODE);
  }
}
