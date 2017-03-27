package com.duy.pascal.frontend.debug;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DebugMonitorAdapter extends RecyclerView.Adapter<DebugMonitorAdapter.ViewHolder> {
    private ArrayList<DebugItem> listData = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public DebugMonitorAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public DebugMonitorAdapter(ArrayList<DebugItem> listData, Context context) {
        this.listData = listData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addLine(DebugItem debugItem) {
        listData.add(debugItem);
        notifyItemInserted(getItemCount() - 1);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_debug_variable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (listData.get(position).getType() == DebugItem.TYPE_VAR) {
            holder.bindMsg(listData.get(position).toString());
        } else {
            holder.bindMsg(listData.get(position).toString());
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void clear() {
        listData.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtName;
        @BindView(R.id.txt_value)
        TextView txtValue;
        @BindView(R.id.background)
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindVar(DebugItem debugItem) {
            txtName.setText(debugItem.getMsg1());
            txtValue.setText(debugItem.getMsg2());
        }

        public void bindMsg(String msg) {
            txtName.setText(msg);
            txtValue.setText("");
        }
    }
}
