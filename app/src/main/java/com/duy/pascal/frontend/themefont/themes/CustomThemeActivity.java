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

package com.duy.pascal.frontend.themefont.themes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.editor.view.EditorView;
import com.duy.pascal.frontend.file.FileManager;
import com.duy.pascal.frontend.themefont.util.CodeTheme;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.IOException;

/**
 * Created by Duy on 12-Jul-17.
 */

public class CustomThemeActivity extends AbstractAppCompatActivity implements View.OnClickListener {
    private EditorView mEditorView;
    private CodeTheme codeTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codeTheme = new CodeTheme(false);
        setContentView(R.layout.acitivty_custom_theme);
        setupToolbar();
        setTitle(getString(R.string.custom_theme));
        bindView();
    }

    private void bindView() {
        mEditorView = (EditorView) findViewById(R.id.editor_view);
        try {
            mEditorView.setText(FileManager.streamToString(getAssets().open("source/preview.pas")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.color_background).setOnClickListener(this);
        findViewById(R.id.color_normal).setOnClickListener(this);
        findViewById(R.id.color_keyword).setOnClickListener(this);
        findViewById(R.id.color_number).setOnClickListener(this);
        findViewById(R.id.color_string).setOnClickListener(this);
        findViewById(R.id.color_comment).setOnClickListener(this);
        findViewById(R.id.color_error).setOnClickListener(this);
        findViewById(R.id.color_opt).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.color_background:
            case R.id.color_normal:
            case R.id.color_keyword:
            case R.id.color_number:
            case R.id.color_string:
            case R.id.color_comment:
            case R.id.color_error:
            case R.id.color_opt:
                setColorForView(id);
                break;
        }
    }

    private void setColorForView(@IdRes final int id) {
        ColorPickerDialogBuilder.with(this).setPositiveButton(android.R.string.ok,
                new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        findViewById(id).setBackgroundColor(lastSelectedColor);
                        setColor(id, lastSelectedColor);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).build().show();
    }

    private void setColor(int id, int color) {
        switch (id) {
            case R.id.color_background:
                codeTheme.putColor("background_color", color);
                break;
            case R.id.color_normal:
                codeTheme.putColor("normal_text_color", color);
                break;
            case R.id.color_keyword:
                codeTheme.putColor("key_word_color", color);
                break;
            case R.id.color_number:
                codeTheme.putColor("number_color", color);
                break;
            case R.id.color_string:
                codeTheme.putColor("string_color", color);
                break;
            case R.id.color_comment:
                codeTheme.putColor("comment_color", color);
                break;
            case R.id.color_error:
                codeTheme.putColor("error_color", color);
                break;
            case R.id.color_opt:
                codeTheme.putColor("opt_color", color);
                break;
        }
        mEditorView.setCodeTheme(codeTheme);
    }


}
