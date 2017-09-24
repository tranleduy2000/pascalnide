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

package com.duy.pascal.frontend.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.duy.pascal.frontend.R;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class DialogFragmentFixExpectToken extends BottomSheetDialogFragment {
    @Nullable
    private DialogFragmentFixExpectToken.OnSelectExpectListener listener;

    @NonNull
    public static DialogFragmentFixExpectToken newInstance(@NonNull ExpectedTokenException e) {
        Bundle bundle = new Bundle();
        bundle.putString("current", e.getCurrent());
        LineInfo line = e.getLineInfo();
        bundle.putInt("line", line.getLine());
        bundle.putInt("column", line.getColumn());
        bundle.putStringArray("expect", e.getExpected());

        DialogFragmentFixExpectToken dialog = new DialogFragmentFixExpectToken();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    public final DialogFragmentFixExpectToken.OnSelectExpectListener getListener() {
        return this.listener;
    }

    public final void setListener(@Nullable DialogFragmentFixExpectToken.OnSelectExpectListener var1) {
        this.listener = var1;
    }

    public void onAttach(@Nullable Context context) {
        super.onAttach(context);
        this.listener = (DialogFragmentFixExpectToken.OnSelectExpectListener) context;
    }

    public void setupDialog(@Nullable Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(this.getContext(), R.layout.dialog_fix_expect, null);
        final String current = this.getArguments().getString("current");
        final int line = this.getArguments().getInt("line");
        final int col = this.getArguments().getInt("column");

        final RadioButton radInsert = view.findViewById(R.id.radio_insert);
        final String[] expects = this.getArguments().getStringArray("expect");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, expects);
        ListView listView = view.findViewById(R.id.list_expect);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public final void onItemClick(AdapterView $noName_0, View $noName_1, int position, long $noName_3) {
                String get = expects[position];
                OnSelectExpectListener var10000 = DialogFragmentFixExpectToken.this.getListener();
                if (var10000 != null) {
                    var10000.onSelectedExpect(current, get, radInsert.isChecked(), line, col);
                }

                DialogFragmentFixExpectToken.this.dismiss();
            }
        });
        if (dialog != null) {
            dialog.setContentView(view);
        }

    }

    public interface OnSelectExpectListener {
        void onSelectedExpect(@NonNull String current, @NonNull String expect, boolean insert, int line, int column);
    }
}
