package com.duy.pascal.frontend.sample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.activities.EditorActivity;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeSampleActivity extends AbstractAppCompatActivity implements CodeSampleAdapter.OnCodeClickListener {

    final String TAG = getClass().getSimpleName();
    private final String[] categories = new String[]{"Basic", "Crt", "Dos", "Graph", "Temp"};
    @BindView(R.id.expand_listview)
    ExpandableListView expandableListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CodeSampleAdapter adapter;
    private ApplicationFileManager fileManager;

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
        expandableListView.setAdapter(adapter);

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


    public class LoadCodeTask extends AsyncTask<Object, Object, ArrayList<CodeCategory>> {
        private ArrayList<CodeCategory> listCodeCategories;

        @Override
        protected ArrayList<CodeCategory> doInBackground(Object... params) {
            listCodeCategories = new ArrayList<>();

            for (int i = 0; i < categories.length; i++) {
                CodeCategory codeCategory = new CodeCategory(categories[i], "");
                if (DLog.DEBUG) Log.d(TAG, "doInBackground: ");
                String[] list;
                String path = "code_sample/" + categories[i].toLowerCase();
                try {
                    list = getAssets().list(path);
                    for (String fileName : list) {
                        if (DLog.DEBUG)
                            Log.d(TAG, "doInBackground: " + fileName);
                        String content = readFile(path + "/" + fileName);
                        codeCategory.addCodeItem(new CodeSampleEntry(fileName, content));
                        Log.d(TAG, "doInBackground: " + content);
                    }
                } catch (IOException ignored) {
                    if (DLog.DEBUG) Log.e(TAG, "doInBackground: ", ignored);
                }
                listCodeCategories.add(codeCategory);
            }

            return listCodeCategories;
        }

        public String readFile(String path) {
            String result = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open(path)));
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    result += mLine + "\n";
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<CodeCategory> aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            adapter.addCodes(listCodeCategories);
        }
    }

}
