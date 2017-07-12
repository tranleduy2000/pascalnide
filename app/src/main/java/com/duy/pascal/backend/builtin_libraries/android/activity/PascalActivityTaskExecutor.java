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

package com.duy.pascal.backend.builtin_libraries.android.activity;

import android.content.Context;
import android.content.Intent;

import com.googlecode.sl4a.Constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PascalActivityTaskExecutor {

    private final Context mContext;
    private final Map<Integer, PascalActivityTask<?>> mTaskMap;
    private final AtomicInteger mIdGenerator;

    public PascalActivityTaskExecutor(Context context) {
        mContext = context;
        mTaskMap = new ConcurrentHashMap<>();
        mIdGenerator = new AtomicInteger(0);
    }

    public void execute(PascalActivityTask<?> task) {
        int id = mIdGenerator.incrementAndGet();
        mTaskMap.put(id, task);
        launchActivity(id);
    }

    public PascalActivityTask<?> popTask(int id) {
        return mTaskMap.remove(id);
    }

    private void launchActivity(int id) {
        Intent helper = new Intent(mContext, PascalActivity.class);
        helper.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        helper.putExtra(Constants.EXTRA_TASK_ID, id);
        mContext.startActivity(helper);
    }
}
