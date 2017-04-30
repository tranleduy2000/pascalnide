package com.duy.pascal.frontend.code_sample;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Duy on 28-Apr-17.
 */

public class FragmentCodeSample extends Fragment {
    private static final String TAG = "FragmentCodeSample";

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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

    private class LoadCodeTask extends AsyncTask<Object, Object, ArrayList<CodeSampleEntry>> {
        private ArrayList<CodeSampleEntry> codeSampleEntries = new ArrayList<>();

        @Override
        protected ArrayList<CodeSampleEntry> doInBackground(Object... params) {
            try {
                String category = getArguments().getString(TAG);
                CodeCategory codeCategory = new CodeCategory(category, "");
                String[] list;
                String path = "code_sample/" + getArguments().getString(TAG).toLowerCase();
                try {
                    AssetManager assets = getContext().getAssets();
                    list = assets.list(path);
                    for (String fileName : list) {
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
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
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
