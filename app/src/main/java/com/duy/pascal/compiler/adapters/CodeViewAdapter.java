package com.duy.pascal.compiler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.alogrithm.AutoIndentCode;
import com.duy.pascal.compiler.utils.ClipboardManager;
import com.duy.pascal.compiler.view.code_view.HighlightEditor;

import java.util.ArrayList;

public class CodeViewAdapter extends RecyclerView.Adapter<CodeViewAdapter.CodeHolder> {
    private ArrayList<String> listCodes = new ArrayList<>();
    private OnCodeClickListener listener;
    private Context context;
    private LayoutInflater inflater;

    public CodeViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public CodeViewAdapter(ArrayList<String> listCodes, Context context) {
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
        HighlightEditor highlightEditor;
        View btnPlay, btnEdit, btnCopy;

        CodeHolder(View itemView) {
            super(itemView);
            highlightEditor = (HighlightEditor) itemView.findViewById(R.id.code_view);
            btnPlay = itemView.findViewById(R.id.img_play);
            btnEdit = itemView.findViewById(R.id.img_edit);
            btnCopy = itemView.findViewById(R.id.img_copy);
        }
    }
}
