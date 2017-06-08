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

package com.duy.pascal.frontend.debug.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.backend.utils.ArrayUtils;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.debug.model.VariableItem;

import java.util.ArrayList;

/**
 * Created by Duy on 08-Jun-17.
 */

public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.VariableHolder> {

    private Context context;
    private ArrayList<VariableItem> variableItems = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public VariableAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<VariableItem> list) {
        variableItems.clear();
        variableItems.addAll(list);
        notifyDataSetChanged();
    }

    public void add(VariableItem item) {
        variableItems.add(item);
        notifyItemInserted(variableItems.size() - 1);
    }

    public void clearData() {
        variableItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public VariableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VariableHolder(layoutInflater.inflate(R.layout.list_item_var, parent, false));
    }

    @Override
    public void onBindViewHolder(VariableHolder holder, int position) {
        holder.txtName.setText(variableItems.get(position).getName());
        holder.txtValue.setText(ArrayUtils.toString(variableItems.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return variableItems.size();
    }

    /**
     * Created by Duy on 08-Jun-17.
     */

    public static class VariableHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtValue;

        public VariableHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtValue = (TextView) itemView.findViewById(R.id.txt_value);
        }
    }
}
