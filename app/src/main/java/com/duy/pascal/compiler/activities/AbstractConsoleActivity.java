package com.duy.pascal.compiler.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.view.ConsoleView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AbstractConsoleActivity extends AbstractAppCompatActivity {
    public final int[] fontSize = {12, 16, 20, 24};
    public int fontSizeNb = 1;
    @BindView(R.id.console)
    public ConsoleView mConsoleView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        ButterKnife.bind(this);
        setupActionBar();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mConsoleView.initConsole(this, fontSize[fontSizeNb] * metrics.density, Color.WHITE, Color.BLACK);
        mConsoleView.setFocusable(true);
        mConsoleView.setFocusableInTouchMode(true);
        mConsoleView.requestFocus();
        mConsoleView.updateSize();

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Execute");
    }

    @Override
    public void onPause() {
        super.onPause();
        mConsoleView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_console, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConsoleView.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_soft:
                showKeyBoard();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_change_keyboard:
                changeKeyBoard();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeKeyBoard() {
        startActivityForResult(
                new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mConsoleView, 0);
    }
}

