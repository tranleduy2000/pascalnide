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

package com.duy.pascal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.duy.pascal.interperter.builtin_libraries.android.activity.PascalActivityTaskExecutor;


public abstract class BasePascalApplication extends MultiDexApplication {
    @NonNull
    private final PascalActivityTaskExecutor taskExecutor = new PascalActivityTaskExecutor((Context) this);

    @NonNull
    public final PascalActivityTaskExecutor getTaskExecutor() {
        return this.taskExecutor;
    }

    public void onCreate() {
        super.onCreate();
    }
}
