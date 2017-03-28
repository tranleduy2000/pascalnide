package com.duy.pascal.frontend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 28-Mar-17.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private static final String TAG = InfoAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private ArrayList<ItemInfo> listData = new ArrayList<>();

    public InfoAdapter(Context context, ArrayList<ItemInfo> listData) {
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_info, parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtTitle.setText(listData.get(position).getTitle());
        holder.txtDesc.setText(listData.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_desc)
        TextView txtDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
