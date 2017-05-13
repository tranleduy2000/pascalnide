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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;

import org.apache.commons.io.FilenameUtils;

import java.util.LinkedList;


public class FileListAdapter extends RecyclerView.Adapter<FileViewHolder> {
    // Layout Inflater
    private final LayoutInflater inflater;
    private final LinkedList<FileDetail> orig;
    private FileAdapterListener fileAdapterListener;
    // List of file details
    private LinkedList<FileDetail> fileDetails;
    private LinkedList<FileDetail> originalFileList;

    @SuppressWarnings("unchecked")
    public FileListAdapter(final Context context,
                           final LinkedList<FileDetail> fileDetails,
                           final boolean isRoot,
                           FileAdapterListener fileAdapterListener) {
        this.fileAdapterListener = fileAdapterListener;
        this.fileDetails = fileDetails;
        this.originalFileList = (LinkedList<FileDetail>) fileDetails.clone();
        this.orig = fileDetails;
        this.inflater = LayoutInflater.from(context);
        if (!isRoot) {
            this.fileDetails.addFirst(new FileDetail("..", context.getString(R.string.folder), ""));
        } else {
            this.fileDetails.addFirst(new FileDetail(context.getString(R.string.home),
                    context.getString(R.string.folder), ""));
        }
    }


    private void setIcon(final FileViewHolder viewHolder, final FileDetail fileDetail) {
        final String fileName = fileDetail.getName();
        final String ext = FilenameUtils.getExtension(fileName);
        if (fileDetail.isFolder()) {
            viewHolder.icon.setImageResource(R.drawable.ic_folder_white_24dp);
        } else if (".pas".contains(ext)) {
            viewHolder.icon.setImageResource(R.drawable.ic_code_white_24dp);
        } else {
            viewHolder.icon.setImageResource(R.drawable.ic_insert_drive_file_white_24dp);
        }
    }


    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_file_list, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        final FileDetail fileDetail = fileDetails.get(position);
        final String fileName = fileDetail.getName();
        setIcon(holder, fileDetail);
        holder.nameLabel.setText(fileName);
        holder.detailLabel.setText(fileDetail.getSize() + "\t\t" + fileDetail.getDateModified());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileAdapterListener != null)
                    fileAdapterListener.onItemClick(v, fileName, FileAdapterListener.ACTION_CLICK);
            }
        });
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (fileAdapterListener != null)
                    fileAdapterListener.onItemClick(v, fileName, FileAdapterListener.ACTION_LONG_CLICK);
                return false;
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileAdapterListener != null)
                    fileAdapterListener.onRemoveClick(v, fileName, FileAdapterListener.ACTION_REMOVE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileDetails.size();
    }

    public boolean query(String newText) {
        fileDetails.clear();
        for (FileDetail fileDetail : originalFileList) {
            if (fileDetail.getName().toLowerCase().contains(newText.toLowerCase())) {
                fileDetails.add(fileDetail);
            }
        }
        notifyDataSetChanged();
        return true;
    }
}
