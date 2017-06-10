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
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.debug.utils.SpanUtils;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.theme.util.CodeTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 08-Jun-17.
 */

public class VariableAdapter extends RecyclerView.Adapter<VariableAdapter.VariableHolder> {

    private Context context;
    private ArrayList<VariableDeclaration> variableItems = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private CodeTheme codeTheme;
    private SpanUtils spanUtils;

    public VariableAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.codeTheme = CodeTheme.getDefault(new PascalPreferences(context));
        this.spanUtils = new SpanUtils(codeTheme);
    }

    public void setData(List<VariableDeclaration> list) {
        variableItems.clear();
        variableItems.addAll(list);
        notifyDataSetChanged();
    }

    public void add(VariableDeclaration item) {
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
        VariableDeclaration var = variableItems.get(position);

        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(spanUtils.generateNameSpan(var.getName()));
        text.append(spanUtils.generateTypeSpan(var.getType(), true));
        text.append(" = ");
        text.append(spanUtils.generateValueSpan(var.getInitialValue(), 10));
        holder.txtName.setText(text);
    }

    @Override
    public int getItemCount() {
        return variableItems.size();
    }

    /**
     * Created by Duy on 08-Jun-17.
     */

    public static class VariableHolder extends RecyclerView.ViewHolder {
        public TextView txtName;

        public VariableHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}
