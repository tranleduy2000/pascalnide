package com.duy.pascal.frontend.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.data.Preferences;
import com.duy.pascal.frontend.view.code_view.CodeView;

import java.util.ArrayList;
import java.util.Collections;

public class ThemeHorizontalAdapter extends PagerAdapter {
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Object> mThemes = new ArrayList<>();
    private Preferences mPreferences;

    public ThemeHorizontalAdapter(final Activity context) {

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPreferences = new Preferences(context);
        addDefaultData();
    }

    private void addDefaultData() {
        Collections.addAll(mThemes, mContext.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
    }

    @Override
    public int getCount() {
        return mThemes.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.code_theme_item, container, false);
        setupItem(view, position);
        container.addView(view);
        return view;
    }

    private void setupItem(View view, final int position) {
        CodeView codeView = (CodeView) view.findViewById(R.id.code_view);
        if ((mThemes.get(position) instanceof String)) {
            codeView.setTheme((String) mThemes.get(position));
        } else {
            codeView.setTheme((int) mThemes.get(position));
        }

        codeView.setTextHighlighted(CodeSample.DEMO_THEME);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(String.valueOf(mThemes.get(position)));
        view.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object o = mThemes.get(position);
                if (o instanceof String) {
                    mPreferences.put(mContext.getString(R.string.key_code_theme),
                            (String) mThemes.get(position));
                } else if (o instanceof Integer) {
                    mPreferences.put(mContext.getString(R.string.key_code_theme),
                            (Integer) mThemes.get(position));
                }
                mContext.finish();
            }
        });
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}