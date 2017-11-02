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

package com.duy.pascal.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.ui.purchase.dialogs.PremiumDialog;

/**
 * Created by Duy on 11/2/2017.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        int themeByName = R.style.AppThemeDark;
        ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), themeByName);
        inflater = LayoutInflater.from(newContext);
        return inflater.inflate(getRootId(), container, false);
    }

    public void showDialogUpgrade() {
        PremiumDialog dialog = PremiumDialog.newInstance();
        dialog.show(getFragmentManager(), PremiumDialog.TAG);
    }

    protected abstract int getRootId();
}
