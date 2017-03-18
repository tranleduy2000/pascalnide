package com.duy.pascal.compiler.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.duy.pascal.compiler.manager.CodeManager;
import com.duy.pascal.compiler.manager.CompileManager;
import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.adapters.CodeViewAdapter;
import com.duy.pascal.compiler.file_manager.FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeSampleActivity extends AppCompatActivity implements CodeViewAdapter.OnCodeClickListener {

    final String TAG = getClass().getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CodeViewAdapter adapter;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_sample);
        ButterKnife.bind(this);
        setTitle(R.string.title_activity_code_sample);
        fileManager = new FileManager(this);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_code_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CodeViewAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);

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
        fileManager.setContentFileTemp(CodeManager.normalCode(code));

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


    public class LoadCodeTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String[] list;
                list = getAssets().list("code_sample");
                for (String path : list) {
                    Log.d(TAG, "doInBackground: " + path);
                    String content = readFile("code_sample/" + path);
                    publishProgress(content);
                }
            } catch (IOException ignored) {
            }
            return null;
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
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            adapter.addCode(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
