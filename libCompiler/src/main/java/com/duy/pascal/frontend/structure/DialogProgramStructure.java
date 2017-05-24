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

package com.duy.pascal.frontend.structure;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.structure.viewholder.StructureItem;
import com.duy.pascal.frontend.structure.viewholder.ViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;

/**
 * Created by Duy on 29-Mar-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class DialogProgramStructure extends AppCompatDialogFragment {
    public static final String TAG = DialogProgramStructure.class.getSimpleName();
    private AndroidTreeView tView;

    public static DialogProgramStructure newInstance(StructureItem node) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, node);
        DialogProgramStructure selectableTreeFragment = new DialogProgramStructure();
        selectableTreeFragment.setArguments(bundle);
        return selectableTreeFragment;
    }

    private TreeNode getTreeNode(StructureItem structureItem) {
        ArrayList<StructureItem> listNode = structureItem.listNode;

        TreeNode treeNode = new TreeNode(new StructureItem(structureItem.type, structureItem.text))
                .setViewHolder(new ViewHolder(getActivity()));

        if (structureItem.listNode.size() == 0) return treeNode;
        for (StructureItem item : listNode) {
            if (item.listNode != null) {
                TreeNode child = getTreeNode(item);
                treeNode.addChild(child);
            }
        }
        return treeNode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_selectable_nodes, container, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        StructureItem rootProgram = (StructureItem) getArguments().getSerializable(TAG);
        TreeNode root = TreeNode.root();
        TreeNode treeNode = getTreeNode(rootProgram);
        root.addChildren(treeNode);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);

        View view = tView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        containerView.removeAllViews();
        containerView.addView(view);

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
