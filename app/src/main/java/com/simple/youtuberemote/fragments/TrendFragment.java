package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simple.youtuberemote.R;
import com.simple.youtuberemote.adapters.HomeAdapter;
import com.simple.youtuberemote.adapters.TrendAdapter;
import com.simple.youtuberemote.models.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loc on 15/04/2018.
 */

public class TrendFragment extends Fragment {
  private TrendAdapter trendAdapter;
  private List<VideoItem> videoList;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_trend, container, false);
    RecyclerView recyclerViewHome = view.findViewById(R.id.recycleViewTrend);
    videoList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    trendAdapter = new TrendAdapter(getActivity(), videoList);
    recyclerViewHome.setLayoutManager(layoutManager);
    recyclerViewHome.setAdapter(trendAdapter);
    return view;
  }
}
