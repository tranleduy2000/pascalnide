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

package com.duy.pascal.ui.purchase.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.duy.pascal.ui.BaseDialogFragment;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.purchase.activities.InAppPurchaseActivity;

/**
 * Created by Duy on 10/5/2017.
 */

public class PremiumDialog extends BaseDialogFragment {
    public static final String TAG = "PremiumDialog";

    public static PremiumDialog newInstance() {

        Bundle args = new Bundle();

        PremiumDialog fragment = new PremiumDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootId() {
        return R.layout.fragment_premium;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickUpgrade();
            }
        });
    }

    private void clickUpgrade() {
        dismiss();
        try {
            InAppPurchaseActivity activity = (InAppPurchaseActivity) getActivity();
            activity.clickUpgrade();
        } catch (Exception ignored) {
        }
    }
}
