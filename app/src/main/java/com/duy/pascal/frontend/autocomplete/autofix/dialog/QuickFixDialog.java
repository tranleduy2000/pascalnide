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

package com.duy.pascal.frontend.autocomplete.autofix.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.autocomplete.autofix.AutoFixHelper;
import com.duy.pascal.frontend.autocomplete.autofix.adapters.CommandAdapter;
import com.duy.pascal.frontend.autocomplete.autofix.command.AutoFixCommand;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.editor.EditorActivity;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import java.util.ArrayList;

/**
 * Created by Duy on 9/28/2017.
 */

public class QuickFixDialog extends BottomSheetDialogFragment {
    public static final String TAG = "QuickFixDialog";
    private static final String KEY_EXCEPTION = "key_exception";
    private Exception exception;

    public static QuickFixDialog newInstance(Exception exception) {
        QuickFixDialog fragment = new QuickFixDialog();
        fragment.setException(exception);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContextThemeWrapper wrap = new ContextThemeWrapper(getActivity(), getActivity().getTheme());
        inflater = LayoutInflater.from(wrap);
        return inflater.inflate(R.layout.dialog_error, container, false);
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExceptionManager exceptionManager = new ExceptionManager(getContext());

        //set title and message for view
        TextView txtTitle = view.findViewById(R.id.txt_name);
        txtTitle.setText(getString(R.string.compile_error));
        TextView txtMsg = view.findViewById(R.id.txt_message);
        txtMsg.setText(exceptionManager.getMessage(exception));

        if (exception instanceof ParsingException && ((ParsingException) exception).canQuickFix()) {
            ListViewCompat listCommand = view.findViewById(R.id.list_command);
            final ArrayList<AutoFixCommand> commands = AutoFixHelper.buildCommands(exception);
            CommandAdapter commandAdapter = new CommandAdapter(getActivity(), R.layout.list_item_quick_fix, commands);
            listCommand.setAdapter(commandAdapter);
            listCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    executeCommand(commands.get(position));
                }
            });
        }
    }

    private void executeCommand(AutoFixCommand autoFixCommand) {
        Log.d(TAG, "executeCommand() called with: autoFixCommand = [" + autoFixCommand + "]");
        try {
            EditorActivity editorActivity = (EditorActivity) getActivity();
            editorActivity.executeCommand(autoFixCommand);
        } catch (Exception ignored) {
        }
        dismiss();
    }
}
