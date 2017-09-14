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

package com.duy.pascal.frontend.dialog

import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.duy.pascal.frontend.R
import com.duy.pascal.frontend.editor.completion.Template
import com.duy.pascal.frontend.editor.completion.model.Patterns
import com.duy.pascal.frontend.file.FileManager
import java.io.File

/**
 * Created by Duy on 10-Apr-17.
 */

class DialogCreateNewFile : AppCompatDialogFragment() {
    private var mEditFileName: EditText? = null
    private var btnOK: Button? = null
    private var btnCancel: Button? = null
    private var listener: OnCreateNewFileListener? = null
    private var checkBoxPas: RadioButton? = null
    private var checkBoxInp: RadioButton? = null
    private var checkBoxUnit: RadioButton? = null
    private var mFileManager: FileManager? = null

    fun setListener(listener: OnCreateNewFileListener?) {
        this.listener = listener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            listener = activity as OnCreateNewFileListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mFileManager = FileManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_new_file, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEditFileName = view!!.findViewById(R.id.edit_file_name)
        mEditFileName!!.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val file = doCreateFile()
                if (listener != null && file != null) {
                    listener!!.onFileCreated(file)
                    listener!!.onCancel()
                }
                dismiss()
                return@OnKeyListener true
            }
            false
        })

        btnOK = view.findViewById(R.id.btn_ok)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnCancel!!.setOnClickListener {
            if (listener != null) listener!!.onCancel()
            dismiss()
        }
        checkBoxPas = view.findViewById(R.id.rad_pas)
        checkBoxInp = view.findViewById(R.id.rad_inp)
        checkBoxUnit = view.findViewById(R.id.rad_unit)

        btnOK!!.setOnClickListener {
            val file = doCreateFile()
            if (listener != null && file != null) {

                listener!!.onFileCreated(file)
                listener!!.onCancel()
                dismiss()
            }
        }

    }

    private fun doCreateFile(): File? {
        //get string path of in edit text
        var fileName = mEditFileName!!.text.toString()
        if (fileName.isEmpty()) {
            mEditFileName!!.error = getString(R.string.enter_new_file_name)
            return null
        }
        if (!(Patterns.FILE_NAME.matcher(fileName).find()
                && !Patterns.KEYWORDS.matcher(fileName).find())) {
            mEditFileName!!.error = "Invalid file name";
            return null;
        }
        if (checkBoxInp!!.isChecked && !fileName.contains(".")) {
            fileName += ".inp"
        } else if ((checkBoxPas!!.isChecked || checkBoxUnit!!.isChecked) && !fileName.contains(".")) {
            fileName += ".pas"
        }
        var file = File(FileManager.getFilePath() + fileName)
        if (file.exists()) {
            mEditFileName!!.error = getString(R.string.file_exist)
            return null
        }
        //create new file
        val filePath = mFileManager!!.createNewFile(FileManager.getFilePath() + fileName)
        file = File(filePath)
        if (checkBoxPas!!.isChecked) {
            mFileManager!!.saveFile(file,
                    Template.createProgramTemplate(file.nameWithoutExtension))
        } else if (checkBoxUnit!!.isChecked) {
            mFileManager!!.saveFile(file,
                    Template.createUnitTemplate(file.nameWithoutExtension))
        }
        return file
    }

    interface OnCreateNewFileListener {
        fun onFileCreated(file: File)

        fun onCancel()
    }

    companion object {
        val TAG = DialogCreateNewFile::class.java.simpleName

        val instance: DialogCreateNewFile
            get() = DialogCreateNewFile()
    }

}
