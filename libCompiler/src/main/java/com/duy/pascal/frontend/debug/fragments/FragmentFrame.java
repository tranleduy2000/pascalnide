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

package com.duy.pascal.frontend.debug.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.debug.CallStack;
import com.duy.pascal.frontend.debug.adapter.FrameAdapter;
import com.duy.pascal.frontend.debug.adapter.VariableAdapter;
import com.duy.pascal.frontend.debug.model.VariableItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 08-Jun-17.
 */

public class FragmentFrame extends Fragment implements FrameAdapter.OnFrameListener {

    private static final String TAG = "FragmentFrame";
    private RecyclerView mListFrame, mListVars;
    private VariableAdapter mVariableAdapter;
    private FrameAdapter mFrameAdapter;

    public static FragmentFrame newInstance() {

        Bundle args = new Bundle();

        FragmentFrame fragment = new FragmentFrame();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListFrame = (RecyclerView) view.findViewById(R.id.rc_frame);
        mListFrame.setHasFixedSize(true);
        mListFrame.setLayoutManager(new LinearLayoutManager(getContext()));
        mFrameAdapter = new FrameAdapter(getContext(), this);
        mListFrame.setAdapter(mFrameAdapter);

        mListVars = (RecyclerView) view.findViewById(R.id.rc_vars);
        mListVars.setHasFixedSize(true);
        mListVars.setLayoutManager(new LinearLayoutManager(getContext()));
        mVariableAdapter = new VariableAdapter(getContext());
        mListVars.setAdapter(mVariableAdapter);
    }

    public void displayFrame(CallStack callStack) {
        mFrameAdapter.setFrames(callStack.getStacks());
    }

    public void displayVars(CallStack callStack) {
        List<String> userDefineVariable = callStack.getUserDefineVariable();
        ArrayList<VariableItem> vars = new ArrayList<>();
        for (String name : userDefineVariable) {
            vars.add(new VariableItem(name, callStack.getValue(name)));
        }
        mVariableAdapter.setData(vars);
    }

    public void update(CallStack callStack) {
        displayFrame(callStack);
        displayVars(callStack);
    }

    @Override
    public void onDestroyView() {
        mFrameAdapter.clearData();
        mVariableAdapter.clearData();
        super.onDestroyView();
    }

    @Override
    public void onSelectFrame(CallStack stack) {
        displayVars(stack);
    }
}