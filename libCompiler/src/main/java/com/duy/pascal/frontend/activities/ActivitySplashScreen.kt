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

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.duy.pascal.frontend.DLog
import com.duy.pascal.frontend.R
import com.duy.pascal.frontend.code.CompileManager
import com.duy.pascal.frontend.editor.EditorActivity
import com.duy.pascal.frontend.file.ApplicationFileManager
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ActivitySplashScreen : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        // Here, this is the current activity
        PreferenceManager.setDefaultValues(this, R.xml.setting_editor, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST)
        } else {
            startMainActivity()
        }
        try {
            val fontses = assets.list("fonts")
            Log.i(TAG, "onCreate: " + Arrays.toString(fontses))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity()
                } else {
                    Toast.makeText(this, R.string.permission_denied_storage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * If receive data from other app (it could be file, text from clipboard),
     * You will be handle data and send to [EditorActivity]
     */
    private fun startMainActivity() {
        val data = intent
        val action = data.action

        if (DLog.DEBUG) DLog.d(TAG, "startMainActivity: action = " + action)

        val type = data.type
        val intentEdit = Intent(this@ActivitySplashScreen, EditorActivity::class.java)
        if (Intent.ACTION_SEND == action && type != null) {

            FirebaseAnalytics.getInstance(this).logEvent("open_from_clipboard", Bundle())

            if (type == "text/plain") {
                handleActionSend(data, intentEdit)
            }

        } else if (Intent.ACTION_VIEW == action && type != null) {

            FirebaseAnalytics.getInstance(this).logEvent("open_from_another", Bundle())

            handleActionView(data, intentEdit)
        } else if (action.equals("run_from_shortcut", ignoreCase = true)) {

            FirebaseAnalytics.getInstance(this).logEvent("run_from_shortcut", Bundle())

            handleRunProgram(data)
            return
        }

        Handler().postDelayed({
            intentEdit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            overridePendingTransition(0, 0)
            startActivity(intentEdit)
            finish()
        }, 400)
    }

    private fun handleRunProgram(data: Intent) {
        val runIntent = Intent(this, ExecuteActivity::class.java)
        runIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        runIntent.putExtra(CompileManager.FILE_PATH,
                data.getStringExtra(CompileManager.FILE_PATH))
        overridePendingTransition(0, 0)
        startActivity(runIntent)
        finish()
    }

    private fun handleActionView(from: Intent,
                                 to: Intent) {
        DLog.d(TAG, "handleActionView() called with: from = [$from], to = [$to]")
        if (from.data.toString().endsWith(".pas")) {
            val uriPath = from.data
            DLog.d(TAG, "handleActionView: " + uriPath.path)
            to.putExtra(CompileManager.FILE_PATH, uriPath.path)
        } else if (from.type == "text/x-pascal") {
            val uri = from.data
            try {
                //clone file
                val inputStream = contentResolver.openInputStream(uri)
                val fileManager = ApplicationFileManager(this)
                val filePath = fileManager.createRandomFile()
                fileManager.copy(inputStream, FileOutputStream(filePath))

                to.putExtra(CompileManager.FILE_PATH, filePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun handleActionSend(from: Intent, to: Intent) {
        val text = from.getStringExtra(Intent.EXTRA_TEXT)

        val fileManager = ApplicationFileManager(this)
        //create new temp file
        val filePath = fileManager.createNewFile(ApplicationFileManager.getApplicationPath() +
                "new_" + Integer.toHexString(System.currentTimeMillis().toInt()) + ".pas")
        fileManager.saveFile(filePath, text)
        to.putExtra(CompileManager.FILE_PATH, filePath)
    }

    companion object {
        private val MY_PERMISSIONS_REQUEST = 11
        private val TAG = ActivitySplashScreen::class.java.simpleName
    }
}
