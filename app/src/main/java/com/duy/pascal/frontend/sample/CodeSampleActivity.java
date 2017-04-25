/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.duy.pascal.frontend.BuildConfig;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.activities.EditorActivity;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeSampleActivity extends AbstractAppCompatActivity implements CodeSampleAdapter.OnCodeClickListener {

    final String TAG = getClass().getSimpleName();

    private final String[] categories;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CodeSampleAdapter adapter;
    private ApplicationFileManager fileManager;

    public CodeSampleActivity() {
        if (!BuildConfig.DEBUG) {
            categories = new String[]{"Basic", "System", "Crt", "Dos", "Graph", "Math", "More"};
        } else {
            categories = new String[]{"android"};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_sample);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_code_sample);

        fileManager = new ApplicationFileManager(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CodeSampleAdapter(this);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        new LoadCodeTask().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPlay(String code) {
        Log.d(TAG, "onPlay: " + code);
        //create file temp
        fileManager.setContentFileTemp(code);

        //set file temp for run
        Intent intent = new Intent(this, ExecuteActivity.class);

        //this code is verified, do not need compile
        intent.putExtra(CompileManager.FILE_PATH, fileManager.getTempFile().getPath());
        startActivity(intent);
    }

    @Override
    public void onEdit(String code) {
        //create file temp
        String file = fileManager.createNewFileInMode("sample_" + Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        fileManager.saveFile(file, code);

        //set file temp for run
        Intent intent = new Intent(this, EditorActivity.class);

        //this code is verified, do not need compile
        intent.putExtra(CompileManager.FILE_PATH, file);
        startActivity(intent);
    }


    public class LoadCodeTask extends AsyncTask<Object, Object, ArrayList<CodeSampleEntry>> {
        private ArrayList<CodeSampleEntry> codeSampleEntries;

        @Override
        protected ArrayList<CodeSampleEntry> doInBackground(Object... params) {
            codeSampleEntries = new ArrayList<>();
            for (String category : categories) {
                CodeCategory codeCategory = new CodeCategory(category, "");
                String[] list;
                String path = "code_sample/" + category.toLowerCase();
                try {
                    list = getAssets().list(path);
                    for (String fileName : list) {
                        String content =
                                ApplicationFileManager.streamToString(getAssets().open(path + "/" + fileName));
                        codeCategory.addCodeItem(new CodeSampleEntry(fileName, content));
                    }
                } catch (IOException ignored) {
                    if (DLog.DEBUG) Log.e(TAG, "doInBackground: ", ignored);
                }
                codeSampleEntries.addAll(codeCategory.getCodeSampleEntries());
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
