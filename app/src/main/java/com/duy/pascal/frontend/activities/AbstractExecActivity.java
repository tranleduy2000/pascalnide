package com.duy.pascal.frontend.activities;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.view.ConsoleView;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class AbstractExecActivity extends AbstractAppCompatActivity {
    private final int[] fontSize = {11, 16, 20, 24};
    public int fontSizeNb = 0;
    @BindView(R.id.console)
    public ConsoleView mConsoleView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
//    @BindView(R.id.list_debug_console)
//    DebugView debugView;

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
        mConsoleView.showPrompt();
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
        menu.findItem(R.id.action_next_line).setVisible(false);
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
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.showInputMethodPicker();
        }
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mConsoleView, 0);
    }
}

