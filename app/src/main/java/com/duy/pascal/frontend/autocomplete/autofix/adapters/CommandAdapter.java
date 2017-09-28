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

package com.duy.pascal.frontend.autocomplete.autofix.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.autocomplete.autofix.command.AutoFixCommand;

import java.util.List;

/**
 * Created by Duy on 9/28/2017.
 */

public class CommandAdapter extends ArrayAdapter<AutoFixCommand> {
    @NonNull
    private final Context context;
    private final int resource;
    @NonNull
    private final List<AutoFixCommand> commandDescriptors;
    private LayoutInflater mInflater;

    public CommandAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<AutoFixCommand> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.commandDescriptors = objects;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_quick_fix, parent, false);
        }
        TextView txtContent = convertView.findViewById(R.id.txt_content);
        txtContent.setText(commandDescriptors.get(position).getTitle(getContext()));
        return convertView;
    }

    @Override
    public int getCount() {
        return commandDescriptors.size();
    }
}
