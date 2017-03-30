package com.duy.pascal.frontend.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.pascal.frontend.BuildConfig;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.data.PascalPreferences;

import java.util.Locale;

public abstract class AbstractAppCompatActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = AbstractAppCompatActivity.class.getSimpleName();
    private static final boolean DEBUG = DLog.DEBUG;
    protected PascalPreferences mPascalPreferences;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPascalPreferences = new PascalPreferences(this);
        setFullScreen();
        setLocale(false);
        setTheme(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * set language
     *
     * @param create
     */
    private void setLocale(boolean create) {
        Locale locale;
        String code = mPascalPreferences.getSharedPreferences().getString(getString(R.string.key_pref_lang), "default_lang");
        if (code.equals("default_lang")) {
          if (DEBUG) Log.d(TAG, "setLocale: default");
            locale = Locale.getDefault();
        } else {
            locale = new Locale(code);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        if (create) recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPascalPreferences != null)
            mPascalPreferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * set theme for app
     *
     * @param recreate -call method onCreate
     */
    protected void setTheme(boolean recreate) {
//        String name = mPreferences.getString(getResources().getString(R.string.key_pref_theme), "");
//        ThemeEngine themeEngine = new ThemeEngine(getApplicationContext());
//        int themeId = themeEngine.getTheme(name);
//        if (themeId != ThemeEngine.THEME_NOT_FOUND) {
//            super.setTheme(themeId);
//            if (recreate) recreate();
//            Log.d(TAG, "Set theme ok");
//        } else {
//            Log.d(TAG, "Theme not found");
//        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (DEBUG)   Log.d(TAG, "onSharedPreferenceChanged: " + s);
//        if (s.equals(getResources().getString(R.string.key_pref_theme))) {
//            setTheme(true);
//            DLog.i("Main: set theme ");
//        } else
        if (s.equals(getString(R.string.key_pref_lang))) {
            setLocale(true);
            Toast.makeText(this, getString(R.string.change_lang_msg), Toast.LENGTH_SHORT).show();
        }
// else if (s.equals(getString(R.string.key_pref_font))) {
//
//            //reload type face
//            FontManager.loadTypefaceFromAsset(this);
//            recreate();
//        }
        if (s.equalsIgnoreCase(getString(R.string.key_full_screen))) {
            setFullScreen();
        }
    }

    private void setFullScreen() {
        if (mPascalPreferences.useFullScreen()) {
            hideStatusBar();
        } else {
            showStatusBar();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPascalPreferences != null)
            mPascalPreferences.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    /**
     * show dialog choose email client
     * send mail
     */
    protected void sendMail() {
//        Intent email = new Intent(Intent.ACTION_SEND);
//        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.dev_mail)});
//        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
//        email.setType("message/rfc822");
//        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    /**
     * share app
     */
    protected void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * show dialog with title and messenger
     *
     * @param title - title
     * @param msg   - messenger
     */
    protected void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton(this.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    /**
     * show dialog with title and messenger
     *
     * @param msg - messenger
     */
    protected void showDialog(String msg) {
        this.showDialog("", msg);
    }


    public void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |

                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    protected void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void moreApp(View view) {
        Uri uri = Uri.parse("market://search?q=pub:Trần Lê Duy");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |

                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=pub:Trần Lê Duy")));
        }
    }

    public void showStatusBar() {

    }

    public void hideStatusBar() {
        if (android.os.Build.VERSION.SDK_INT < 26) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
