package com.duy.pascal.frontend.code_editor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.v4.ArrayPagerAdapter;

import java.util.List;

/**
 * Created by Duy on 29-Apr-17.
 */

public class EditorPagerAdapter extends ArrayPagerAdapter<Fragment> {
    private int MAX_PAGE = 5;

    public EditorPagerAdapter(FragmentManager fragmentManager, List<PageDescriptor> descriptors) {
        super(fragmentManager, descriptors);
    }


    @Override
    protected Fragment createFragment(PageDescriptor pageDescriptor) {
        return EditorFragment.newInstance(pageDescriptor.getFragmentTag());
    }

}
