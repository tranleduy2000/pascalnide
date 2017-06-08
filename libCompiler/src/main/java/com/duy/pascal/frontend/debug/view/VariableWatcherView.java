/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.debug.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.duy.pascal.frontend.debug.adapter.ValueWatcherAdapter;
import com.duy.pascal.frontend.debug.model.VariableItem;

/**
 * Created by Duy on 22-Apr-17.
 */

public class VariableWatcherView extends RecyclerView {
    private ValueWatcherAdapter mAdapter;
    private View emptyView;

    public VariableWatcherView(Context context) {
        super(context);
        setup(context);
    }

    public VariableWatcherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public VariableWatcherView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        mAdapter = new ValueWatcherAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(false);
        setAdapter(mAdapter);
    }

    /**
     * clear DEBUG
     */
    public void clear() {
        mAdapter.clear();
    }

    public void addVariable(VariableItem variableItem) {
        mAdapter.addVariable(variableItem);
        if (emptyView != null) emptyView.setVisibility(GONE);
        scrollToPosition(mAdapter.getItemCount() - 1);
    }


    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
