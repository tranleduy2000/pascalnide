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

package com.duy.pascal.ui.autocomplete.autofix.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.command.AutoFixCommand;

import java.util.List;

/**
 * Created by Duy on 9/28/2017.
 */

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.ViewHolder> {
    @NonNull
    private Context context;
    @NonNull
    private List<AutoFixCommand> commandDescriptors;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public CommandAdapter(@NonNull Context context, @NonNull List<AutoFixCommand> objects) {
        this.context = context;
        this.commandDescriptors = objects;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.list_item_quick_fix, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView txtContent = holder.txtContent;
        txtContent.setTypeface(Typeface.MONOSPACE);
        txtContent.setText(commandDescriptors.get(position).getTitle(context));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(commandDescriptors.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandDescriptors.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(AutoFixCommand command);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        View root;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txt_content);
            root = itemView.findViewById(R.id.root);
        }
    }
}
