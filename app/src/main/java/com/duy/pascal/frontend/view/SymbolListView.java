package com.duy.pascal.frontend.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.duy.pascal.frontend.adapters.SymbolAdapter;
import com.duy.pascal.frontend.data.PatternUtil;

import java.util.ArrayList;

/**
 * Created by Duy on 11-Feb-17.
 */

public class SymbolListView extends RecyclerView {
    private OnKeyListener mListener;
    private ArrayList<String> mKey = new ArrayList<>();
    private SymbolAdapter mAdapter;

    public SymbolListView(Context context) {
        super(context);
        setup(context);
    }

    public SymbolListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public SymbolListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);

    }

    public OnKeyListener getListener() {
        return mListener;
    }

    public void setListener(OnKeyListener mListener) {
        this.mListener = mListener;
        mAdapter.setListener(mListener);
    }

    private void setup(Context context) {
        mAdapter = new SymbolAdapter();
        mAdapter.setListKey(PatternUtil.symbols_key);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(false);
        setAdapter(mAdapter);
    }

    public interface OnKeyListener {
        void onKeyClick(View view, String text);

        void onKeyLongClick(String text);
    }
}
