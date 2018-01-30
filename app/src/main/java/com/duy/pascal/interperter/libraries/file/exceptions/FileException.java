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

package com.duy.pascal.interperter.libraries.file.exceptions;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.ui.R;

import java.io.File;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;

/**
 * Created by Duy on 13-Apr-17.
 */

public abstract class FileException extends RuntimePascalException {
    public String filePath = "";

    public FileException(@NonNull String filePath) {
        this.filePath = filePath;
    }

    public FileException(@NonNull File file) {
        this.filePath = file.getPath();
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        FileException e = this;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(formatLine(context, e.getLineNumber()));
        builder.append("\n\n");
        builder.append(context.getString(R.string.file));
        builder.append(": ");
        builder.append(e.filePath);
        builder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0,
                builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("\n");
        builder.append("\n");

        if (e instanceof DiskReadErrorException) {
            builder.append(context.getString(R.string.DiskReadErrorException));
        } else if (e instanceof FileNotAssignException) {
            builder.append(context.getString(R.string.FileNotAssignException));
        } else if (e instanceof com.duy.pascal.interperter.libraries.file.exceptions.FileNotFoundException) {
            builder.append(context.getString(R.string.FileNotFoundException));
        } else if (e instanceof FileNotOpenException) {
            builder.append(context.getString(R.string.FileNotOpenException));
        } else if (e instanceof FileNotOpenForInputException) {
            builder.append(context.getString(R.string.FileNotOpenForInputException));
        }
        return builder;
    }
}
