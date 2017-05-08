package com.duy.pascal.frontend.code_sample;

import android.support.v4.app.FragmentManager;

import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.v4.ArrayPagerAdapter;

import java.util.List;

/**
 * Created by Duy on 28-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class CodePagerAdapter extends ArrayPagerAdapter<FragmentCodeSample> {
    public CodePagerAdapter(FragmentManager fragmentManager, List<PageDescriptor> descriptors) {
        super(fragmentManager, descriptors);

    }

    @Override
    protected FragmentCodeSample createFragment(PageDescriptor desc) {
        return FragmentCodeSample.newInstance(desc.getTitle());
    }
}
