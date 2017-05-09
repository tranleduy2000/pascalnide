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

package com.duy.pascal.frontend.file.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duy.pascal.frontend.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewHolder extends RecyclerView.ViewHolder {

    // Name of the file
    @BindView(R.id.txt_name)
    public TextView nameLabel;

    // Size of the file
    @BindView(R.id.txt_info)
    public TextView detailLabel;

    // Icon of the file
    @BindView(R.id.img_icon)
    public ImageView icon;

    @BindView(R.id.img_delete)
    View imgDelete;
    @BindView(R.id.container)
    View root;

    public FileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
