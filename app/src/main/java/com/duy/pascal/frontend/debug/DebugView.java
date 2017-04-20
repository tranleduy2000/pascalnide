/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.debug;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Duy on 11-Feb-17.
 */

public class DebugView extends RecyclerView {
    private DebugMonitorAdapter mAdapter;

    public DebugView(Context context) {
        super(context);
        setup(context);
    }

    public DebugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public DebugView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);

    }

    private void setup(Context context) {
        mAdapter = new DebugMonitorAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(false);
        setAdapter(mAdapter);
    }

    /**
     * clear debug
     */
    public void clear() {
        mAdapter.clear();
    }

    public void addLine(DebugItem debugItem) {
        mAdapter.addLine(debugItem);
        scrollToPosition(mAdapter.getItemCount() - 1);
    }
}
