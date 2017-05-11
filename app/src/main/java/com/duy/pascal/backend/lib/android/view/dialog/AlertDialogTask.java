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

package com.duy.pascal.backend.lib.android.view.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.duy.pascal.backend.imageprocessing.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Wrapper class for alert dialog running in separate thread.
 *
 * @author MeanEYE.rcf (meaneye.rcf@gmail.com)
 */
public class AlertDialogTask extends DialogTask {

    private final String mTitle;
    private final String mMessage;
    private final List<String> mItems;
    private final Set<Integer> mSelectedItems;
    private final Map<String, Object> mResultMap;
    private InputType mInputType;
    private int mEditInputType = 0;

    private String mPositiveButtonText;
    private String mNegativeButtonText;
    private String mNeutralButtonText;

    private EditText mEditText;
    private String mDefaultText;
    @Nullable
    private String mHint;

    public AlertDialogTask(String title, String message) {
        mTitle = title;
        mMessage = message;
        mInputType = InputType.DEFAULT;
        mItems = new ArrayList<>();
        mSelectedItems = new TreeSet<>();
        mResultMap = new HashMap<>();
    }

    public void setPositiveButtonText(String text) {
        mPositiveButtonText = text;
    }

    public void setNegativeButtonText(String text) {
        mNegativeButtonText = text;
    }

    public void setNeutralButtonText(String text) {
        mNeutralButtonText = text;
    }

    /**
     * Set list items.
     */
    public void setItems(JSONArray items) {
        mItems.clear();
        for (int i = 0; i < items.length(); i++) {
            try {
                mItems.add(items.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        mInputType = InputType.MENU;
    }

    /**
     * Set single choice items.
     *
     * @param items    a list of items as {@link String}s to display
     * @param selected the index of the item that is selected by default
     */
    public void setSingleChoiceItems(JSONArray items, int selected) {
        setItems(items);
        mSelectedItems.clear();
        mSelectedItems.add(selected);
        mInputType = InputType.SINGLE_CHOICE;
    }

    /**
     * Set multi choice items.
     *
     * @param items    a list of items as {@link String}s to display
     * @param selected a list of indices for items that should be selected by default
     * @throws JSONException
     */
    public void setMultiChoiceItems(JSONArray items, JSONArray selected) throws JSONException {
        setItems(items);
        mSelectedItems.clear();
        if (selected != null) {
            for (int i = 0; i < selected.length(); i++) {
                mSelectedItems.add(selected.getInt(i));
            }
        }
        mInputType = InputType.MULTI_CHOICE;
    }

    /**
     * Returns the list of selected items.
     */
    public Set<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    public void setTextInput(String defaultText) {
        mDefaultText = defaultText;
        mInputType = InputType.PLAIN_TEXT;
        setEditInputType("text");
    }

    public void setEditInputType(String editInputType) {
        String[] list = editInputType.split("\\|");
        Map<String, Integer> types = ViewInflater.getInputTypes();
        mEditInputType = 0;
        for (String flag : list) {
            Integer v = types.get(flag.trim());
            if (v != null) {
                mEditInputType |= v;
            }
        }
        if (mEditInputType == 0) {
            mEditInputType = android.text.InputType.TYPE_CLASS_TEXT;
        }
    }

    public void setPasswordInput() {
        mInputType = InputType.PASSWORD;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mTitle != null) {
            builder.setTitle(mTitle);
        }
        // Can't display both a message and items. We'll elect to show the items instead.
        if (mMessage != null && mItems.isEmpty()) {
            builder.setMessage(mMessage);
        }
        switch (mInputType) {
            // Add single choice menu items to dialog.
            case SINGLE_CHOICE:
                builder.setSingleChoiceItems(getItemsAsCharSequenceArray(), mSelectedItems.iterator().next(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                mSelectedItems.clear();
                                mSelectedItems.add(item);
                            }
                        });
                break;
            // Add multiple choice items to the dialog.
            case MULTI_CHOICE:
                boolean[] selectedItems = new boolean[mItems.size()];
                for (int i : mSelectedItems) {
                    selectedItems[i] = true;
                }
                builder.setMultiChoiceItems(getItemsAsCharSequenceArray(), selectedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(item);
                                } else {
                                    mSelectedItems.remove(item);
                                }
                            }
                        });
                break;
            // Add standard, menu-like, items to dialog.
            case MENU:
                builder.setItems(getItemsAsCharSequenceArray(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Map<String, Integer> result = new HashMap<>();
                        result.put("item", item);
                        dismissDialog();
                        setResult(result);
                    }
                });
                break;
            case PLAIN_TEXT:
                setViewTextInput(builder);
                break;
            case PASSWORD:
                setViewPasswordInput(builder);
                break;
            default:
                // No input operator specified.
        }
        configureButtons(builder, getActivity());
        addOnCancelListener(builder, getActivity());
        mDialog = builder.show();
        mShowLatch.countDown();
    }

    private void setViewPasswordInput(AlertDialog.Builder builder) {
        mEditText = new EditText(getActivity());
        mEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.setTransformationMethod(new PasswordTransformationMethod());
        builder.setView(mEditText);
    }

    private void setViewTextInput(AlertDialog.Builder builder) {
        //create TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(getActivity());
        textInputLayout.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        if (mHint != null) {
            textInputLayout.setHint(mHint);
        }

        //create EditText
        mEditText = new EditText(getActivity());
        mEditText.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        if (mDefaultText != null) {
            mEditText.setText(mDefaultText);
        }
        mEditText.setInputType(mEditInputType);
        textInputLayout.addView(mEditText);
        int padding = (int) ImageUtils.convertDpToPixel(16, getActivity());
        textInputLayout.setPadding(padding, padding, padding, padding);

        //set custom view
        builder.setView(textInputLayout);
    }

    private CharSequence[] getItemsAsCharSequenceArray() {
        return mItems.toArray(new CharSequence[mItems.size()]);
    }

    private AlertDialog.Builder addOnCancelListener(final AlertDialog.Builder builder, final Activity activity) {
        return builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mResultMap.put("canceled", true);
                setResult();
            }
        });
    }

    private void configureButtons(final AlertDialog.Builder builder, final Activity activity) {
        DialogInterface.OnClickListener buttonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mResultMap.put("which", "positive");
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mResultMap.put("which", "negative");
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        mResultMap.put("which", "neutral");

                        break;
                }
                setResult();
            }
        };
        if (mNegativeButtonText != null) {
            builder.setNegativeButton(mNegativeButtonText, buttonListener);
        }
        if (mPositiveButtonText != null) {
            builder.setPositiveButton(mPositiveButtonText, buttonListener);
        }
        if (mNeutralButtonText != null) {
            builder.setNeutralButton(mNeutralButtonText, buttonListener);
        }
    }

    private void setResult() {
        dismissDialog();
        if (mInputType == InputType.PLAIN_TEXT || mInputType == InputType.PASSWORD) {
            mResultMap.put("value", mEditText.getText().toString());
        }
        setResult(mResultMap);
    }

    public void setHint(String hint) {
        this.mHint = hint;
    }

    private enum InputType {
        DEFAULT, MENU, SINGLE_CHOICE, MULTI_CHOICE, PLAIN_TEXT, PASSWORD
    }

}
