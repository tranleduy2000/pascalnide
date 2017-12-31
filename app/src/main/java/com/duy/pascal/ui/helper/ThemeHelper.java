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

package com.duy.pascal.ui.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.duy.pascal.ui.R;

/**
 * Created by Duy on 11/2/2017.
 */
public class ThemeHelper {
    @NonNull
    public static LayoutInflater wrap(@NonNull Context context) {
        int themeByName = R.style.AppThemeDark;
        ContextThemeWrapper newContext = new ContextThemeWrapper(context, themeByName);
        return LayoutInflater.from(newContext);
    }
}
