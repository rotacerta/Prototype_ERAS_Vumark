package com.vuforia.Navigation.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vuforia.Navigation.Fragments.PageCamera;
import com.vuforia.Navigation.Fragments.PageListProducts;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private String[] TITLES;

    public PagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.TITLES = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PageCamera();
            case 1: return new PageListProducts();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
