package com.kalis.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kalis on 1/16/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentsList = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size() > 0 ? fragmentsList.size() : 0;
    }

    public void addFragment(Fragment f)
    {
     fragmentsList.add(f);

    }

    public void clearFragment()
    {
        fragmentsList.clear();
    }
}
