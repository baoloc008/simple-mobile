package com.simple.youtuberemote.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simple.youtuberemote.R;


/**
 * Created by loc on 15/04/2018.
 */

public class PlaylistFragment extends Fragment
{
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_playlist, container, false);
    return view;
  }
}