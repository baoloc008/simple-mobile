package com.simple.youtuberemote.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.simple.youtuberemote.fragments.HomeFragment;
import com.simple.youtuberemote.fragments.PlaylistFragment;
import com.simple.youtuberemote.fragments.TrendFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by loc on 15/04/2018.
 */

public class ViewPagerHomeAdapter extends FragmentPagerAdapter
{
  private List<Fragment> listFragment = new ArrayList<>();
  private Context context;

  public ViewPagerHomeAdapter(FragmentManager fm, Context context)
  {
    super(fm);
    this.context = context;
    addList();
  }

  @Override
  public Fragment getItem(int position)
  {
    return listFragment.get(position);
  }

  @Override
  public int getCount()
  {
    return listFragment.size();
  }

  @Override
  public CharSequence getPageTitle(int position)
  {
    return null;
  }

  private void addList()
  {
    // add Fragment
    listFragment.add(new HomeFragment());
    listFragment.add(new TrendFragment());
    listFragment.add(new PlaylistFragment());
  }
}
