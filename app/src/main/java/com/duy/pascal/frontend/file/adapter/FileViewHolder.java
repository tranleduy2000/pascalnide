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
