package com.duy.pascal.frontend.sample;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.file.ApplicationFileManager;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Duy on 28-Apr-17.
 */

public class FragmentCodeSample extends Fragment {
    private static final String TAG = "FragmentCodeSample";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private Unbinder unbinder;

    private CodeSampleAdapter adapter;

    public static FragmentCodeSample newInstance(String category) {
        FragmentCodeSample fragmentCodeSample = new FragmentCodeSample();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, category);
        fragmentCodeSample.setArguments(bundle);
        return fragmentCodeSample;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_sample, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapter = new CodeSampleAdapter(getContext());
        try {
            adapter.setListener((CodeSampleAdapter.OnCodeClickListener) getActivity());
        } catch (Exception ignored) {
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LoadCodeTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private class LoadCodeTask extends AsyncTask<Object, Object, ArrayList<CodeSampleEntry>> {
        private ArrayList<CodeSampleEntry> codeSampleEntries;

        @Override
        protected ArrayList<CodeSampleEntry> doInBackground(Object... params) {
            Log.d(TAG, "doInBackground: ");
            codeSampleEntries = new ArrayList<>();
            String category = getArguments().getString(TAG);
            CodeCategory codeCategory = new CodeCategory(category, "");
            String[] list;
            String path = "code_sample/" + getArguments().getString(TAG).toLowerCase();
            Log.d(TAG, "doInBackground: " + path);
            try {
                AssetManager assets = getContext().getAssets();
                list = assets.list(path);
                for (String fileName : list) {
                    Log.d(TAG, "doInBackground: " + fileName);
                    if (fileName.endsWith(".pas")) {
                        String content =
                                ApplicationFileManager.streamToString(assets.open(path + "/" + fileName));
                        codeCategory.addCodeItem(new CodeSampleEntry(fileName, content));
                    }
                }
            } catch (IOException ignored) {
                Dlog.e(ignored);
            }
            codeSampleEntries.addAll(codeCategory.getCodeSampleEntries());
            return codeSampleEntries;
        }


        @Override
        protected void onPostExecute(ArrayList<CodeSampleEntry> aVoid) {
            super.onPostExecute(aVoid);
            adapter.addCodes(codeSampleEntries);
            adapter.notifyDataSetChanged();
        }
    }
}
