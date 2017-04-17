package com.duy.pascal.frontend.program_structure.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.duy.pascal.frontend.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Uses for program, function and procedure node
 * <p>
 * Created by Duy on 29-Mar-17.
 */
public class ViewHolder extends TreeNode.BaseNodeViewHolder<StructureItem> {

    public ViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, StructureItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);

        TextView tvValue = (TextView) view.findViewById(R.id.txt_name);
        tvValue.setText(value.text);

        final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        String prefix = StructureType.ICONS[value.type];

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(StructureType.COLORS_FOREGROUND[value.type]).bold()
                .withBorder(3)
                .endConfig()
                .buildRound(prefix, StructureType.COLORS_BACKGROUND[value.type]);
        iconView.setImageDrawable(drawable);

        return view;
    }

    @Override
    public void toggle(boolean active) {

    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }
}
