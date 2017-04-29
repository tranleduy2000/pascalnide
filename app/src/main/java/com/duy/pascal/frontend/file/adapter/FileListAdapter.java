/*
 * Copyright (C) 2014 Vlad Mihalachi
 *
 * This file is part of Turbo Editor.
 *
 * Turbo Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Turbo Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    public FileListAdapter(final Context context,
                           final LinkedList<FileDetail> fileDetails,
                           final boolean isRoot,
                           FileAdapterListener fileAdapterListener) {
        this.fileAdapterListener = fileAdapterListener;
        this.fileDetails = fileDetails;
        this.orig = fileDetails;
        this.inflater = LayoutInflater.from(context);
        if (!isRoot) {
            this.fileDetails.addFirst(new FileDetail("..", context.getString(R.string.folder), ""));
        } else {
            this.fileDetails.addFirst(new FileDetail(context.getString(R.string.home), context.getString(R.string.folder), ""));
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
/*
    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = orig;
                results.count = orig.size();
            } else {
                LinkedList<FileDetail> nHolderList = new LinkedList<>();
                for (FileDetail h : orig) {
                    if (h.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                        nHolderList.add(h);
                }
                results.values = nHolderList;
                results.count = nHolderList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fileDetails = (LinkedList<FileDetail>) results.values;
            notifyDataSetChanged();
        }
    }*/
}
