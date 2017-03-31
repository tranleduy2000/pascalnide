package com.duy.pascal.frontend.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.code_view.CodeView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeThemeAdapter extends RecyclerView.Adapter<CodeThemeAdapter.ViewHolder> {
    private ArrayList<Object> mThemes = new ArrayList<>();
    private LayoutInflater inflater;
    private PascalPreferences mPascalPreferences;
    private Activity context;

    public CodeThemeAdapter(Activity context) {
        Collections.addAll(mThemes, context.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.code_theme_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if ((mThemes.get(position) instanceof String)) {
            holder.codeView.setTheme((String) mThemes.get(position));
        } else {
            holder.codeView.setTheme((int) mThemes.get(position));
        }

        holder.codeView.setTextHighlighted(CodeSample.DEMO_THEME);
//        holder.codeView.applyTabWidth();
        holder.txtTitle.setText(String.valueOf(mThemes.get(position)));
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPascalPreferences.put(context.getString(R.string.key_code_theme),
                        String.valueOf(mThemes.get(position)));
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.code_view)
        CodeView codeView;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.btn_select)
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
