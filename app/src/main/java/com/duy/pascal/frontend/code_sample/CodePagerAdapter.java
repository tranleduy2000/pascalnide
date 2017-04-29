package com.duy.pascal.frontend.code_sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Duy on 28-Apr-17.
 */

public class CodePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> categories = new ArrayList<>();

    public CodePagerAdapter(FragmentManager fm, String[] categories) {
        super(fm);
        Collections.addAll(this.categories, categories);
    }

    @Override
    public Fragment getItem(int position) {
        String category = categories.get(position);
        return FragmentCodeSample.newInstance(category);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position);
    }
}
