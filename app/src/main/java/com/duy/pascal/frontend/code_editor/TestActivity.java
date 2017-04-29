package com.duy.pascal.frontend.code_editor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.duy.pascal.frontend.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

//        ArrayList<File> tabFiles = TabFileUtils.getTabFiles(this);
//        EditorPagerAdapter pagerAdapter
//                = new EditorPagerAdapter(getSupportFragmentManager(), tabFiles);
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

}
