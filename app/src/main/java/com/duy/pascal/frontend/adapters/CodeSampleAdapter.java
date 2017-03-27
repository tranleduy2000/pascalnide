package com.duy.pascal.frontend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.alogrithm.AutoIndentCode;
import com.duy.pascal.frontend.utils.ClipboardManager;
import com.duy.pascal.frontend.view.code_view.CodeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeSampleAdapter extends RecyclerView.Adapter<CodeSampleAdapter.CodeHolder> {
    private ArrayList<String> listCodes = new ArrayList<>();
    private OnCodeClickListener listener;
    private Context context;
    private LayoutInflater inflater;

    public CodeSampleAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public CodeSampleAdapter(ArrayList<String> listCodes, Context context) {
        this.listCodes = listCodes;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addCode(String code) {
        code = AutoIndentCode.format(code);
        this.listCodes.add(code);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addCode(CodeEntry codeEntry) {

    }

    @Override
    public CodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.code_view_item, parent, false);
        return new CodeHolder(view);
    }

    @Override
    public void onBindViewHolder(CodeHolder holder, final int position) {
        holder.highlightEditor.setText(listCodes.get(position));
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlay(listCodes.get(position));
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEdit(listCodes.get(position));
            }
        });
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, listCodes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCodes.size();
    }

    public OnCodeClickListener getListener() {
        return listener;
    }

    public void setListener(OnCodeClickListener listener) {
        this.listener = listener;
    }


    public interface OnCodeClickListener {
        void onPlay(String code);

        //            void onCopy(String code);
        void onEdit(String code);
    }

    class CodeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.code_view)
        CodeView highlightEditor;
        @BindView(R.id.img_play)
        View btnPlay;
        @BindView(R.id.img_edit)
        View btnEdit;
        @BindView(R.id.img_copy)
        View btnCopy;

        CodeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            highlightEditor.setCanEdit(false);
        }
    }
}
