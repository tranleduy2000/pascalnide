package com.duy.pascal.frontend.sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.view.code_view.CodeView;
import com.ramotion.foldingcell.FoldingCell;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 20-Apr-17.
 */

class CodeHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_play)
    View btnPlay;
    @BindView(R.id.img_edit)
    View btnEdit;
    @BindView(R.id.img_copy)
    View btnCopy;
    @BindView(R.id.code_view)
    CodeView codeView;
    @BindView(R.id.code_view_small)
    CodeView codeViewSmall;
    @BindView(R.id.folding_cell)
    FoldingCell foldingCell;

    public CodeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
