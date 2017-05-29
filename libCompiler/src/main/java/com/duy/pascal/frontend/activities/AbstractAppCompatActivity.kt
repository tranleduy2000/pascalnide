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

package com.duy.pascal.frontend.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.duy.pascal.BasePascalApplication
import com.duy.pascal.frontend.DLog
import com.duy.pascal.frontend.R
import com.duy.pascal.frontend.setting.PascalPreferences
import java.util.*

abstract class AbstractAppCompatActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    protected var mPascalPreferences: PascalPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPascalPreferences = PascalPreferences(this)
        setFullScreen()
        setLocale(false)
        setTheme(false)
    }


    /**
     * set language
     */
    private fun setLocale(create: Boolean) {
        val locale: Locale
        val code = mPascalPreferences!!.sharedPreferences.getString(getString(R.string.key_pref_lang), "default_lang")
        if (code == "default_lang") {
            if (DEBUG) DLog.d(TAG, "setLocale: default")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().configuration.locales.get(0)
            } else {
                locale = Resources.getSystem().configuration.locale
            }
        } else {
            locale = Locale(code)
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        val resources = resources
        resources.updateConfiguration(config, resources.displayMetrics)
        if (create) recreate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    override fun onStart() {
        super.onStart()
        if (mPascalPreferences != null)
            mPascalPreferences!!.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()

    }

    /**
     * set theme for app

     * @param recreate -call method onCreate
     */
    protected fun setTheme(recreate: Boolean) {}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        if (DEBUG) DLog.d(TAG, "onSharedPreferenceChanged: " + s)
        if (s == getString(R.string.key_pref_lang)) {
            setLocale(true)
            //            Toast.makeText(this, readBuffer(R.string.change_lang_msg), Toast.LENGTH_SHORT).show();
        } else if (s.equals(getString(R.string.key_full_screen), ignoreCase = true)) {
            setFullScreen()
        }
    }

    fun setFullScreen() {
        if (mPascalPreferences!!.useFullScreen()) {
            hideStatusBar()
        } else {
            showStatusBar()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        DLog.d(TAG, "onDestroy: ")
        if (mPascalPreferences != null)
            mPascalPreferences!!.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * share app
     */
    protected fun shareApp() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + (application as BasePascalApplication).applicationID)
        intent.type = "text/plain"
        startActivity(intent)
    }

    /**
     * show dialog with title and messenger

     * @param title - title
     * *
     * @param msg   - messenger
     */
    protected fun showDialog(title: String, msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(msg)
        builder.setNegativeButton(this.getString(R.string.close)) { dialogInterface, i -> dialogInterface.cancel() }
        builder.create().show()
    }

    /**
     * show dialog with title and messenger

     * @param msg - messenger
     */
    protected fun showDialog(msg: String) {
        this.showDialog("", msg)
    }

    fun goToPlayStore() {
        this.goToPlayStore(null)
    }

    fun goToPlayStore(view: View?) {
        val uri = Uri.parse("market://details?id=" + (application as BasePascalApplication).applicationID)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + (application as BasePascalApplication).applicationID)))
        }

    }

    protected fun hideKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun moreApp() {
        val uri = Uri.parse("market://search?q=pub:Trần Lê Duy")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=pub:Trần Lê Duy")))
        }

    }

    fun showStatusBar() {
        if (Build.VERSION.SDK_INT < 30) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun hideStatusBar() {
        // TODO: 30-Mar-17
        if (android.os.Build.VERSION.SDK_INT < 30) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        } else {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        }
    }

    /**
     * set support action bar for activity
     */
    protected open fun setupToolbar() {
        val toolbar: Toolbar
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        setTitle(R.string.theme)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        val TAG = AbstractAppCompatActivity::class.java.simpleName
        private val DEBUG = DLog.DEBUG
    }
}
