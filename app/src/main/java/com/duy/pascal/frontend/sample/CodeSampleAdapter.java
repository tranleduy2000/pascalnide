package com.duy.pascal.frontend.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.ClipboardManager;
import com.duy.pascal.frontend.view.code_view.CodeView;

import java.util.ArrayList;

/**
 * Created by Duy on 08-Apr-17.
 */

public class CodeSampleAdapter extends BaseExpandableListAdapter {
    private ArrayList<CodeCategory> codeCategories = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private OnCodeClickListener listener;

    public CodeSampleAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return codeCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return codeCategories.get(groupPosition).getCodeSize();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return codeCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return codeCategories.get(groupPosition).getCode(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CodeCategory headerTitle = (CodeCategory) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.header_code_sample_category, parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
        txtTitle.setText(headerTitle.getTitle());

        TextView txtDesc = (TextView) convertView.findViewById(R.id.txt_summary);
        txtDesc.setText(headerTitle.getDescription());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.code_view_item, parent, false);
        }
        CodeView highlightEditor = (CodeView) convertView.findViewById(R.id.code_view);
        final CodeSampleEntry codeSampleEntry = (CodeSampleEntry) getChild(groupPosition, childPosition);
        highlightEditor.setTextHighlighted(codeSampleEntry.getContent());
        highlightEditor.setCanEdit(false);

        View btnPlay = convertView.findViewById(R.id.img_play);
        View btnEdit = convertView.findViewById(R.id.img_edit);
        View btnCopy = convertView.findViewById(R.id.img_copy);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlay(codeSampleEntry.getContent());
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEdit(codeSampleEntry.getContent());
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, codeSampleEntry.getContent());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setListener(OnCodeClickListener listener) {
        this.listener = listener;
    }

    public void addCodes(ArrayList<CodeCategory> listCodeCategories) {
        this.codeCategories.addAll(listCodeCategories);
    }


    public interface OnCodeClickListener {
        void onPlay(String code);

        //            void onCopy(String code);
        void onEdit(String code);
    }

}
