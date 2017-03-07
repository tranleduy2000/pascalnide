package com.duy.pascal.compiler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.compiler.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Duy on 11-Feb-17.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<File> mFileList = new ArrayList<>();
    private View mEmptyView;
    private FileListener listener;
    private LayoutInflater inflater;

    public FileAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setFileList(ArrayList<File> mFileList) {
        this.mFileList = mFileList;
        if (mFileList.size() == 0) {
            if (mEmptyView != null) mEmptyView.setVisibility(View.VISIBLE);
        } else {
            if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.file_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final File file = mFileList.get(position);
        holder.txtName.setText(file.getName());
        holder.txtPath.setText(file.getPath());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onFileClick(file);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onFileLongClick(file);
                return false;
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.doRemoveFile(file);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public void setEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    public void setListener(FileListener listener) {
        this.listener = listener;
    }

    public interface FileListener {
        void onFileClick(File file);

        void onFileLongClick(File file);

        boolean doRemoveFile(File file);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPath;
        View cardView, imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtPath = (TextView) itemView.findViewById(R.id.txt_path);
            cardView = itemView.findViewById(R.id.card_view);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}
