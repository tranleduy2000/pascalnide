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

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.io.File;

/**
 * Created by Duy on 13-Apr-17.
 */

public abstract class FileException extends RuntimePascalException {
    public String filePath = "";

    public FileException(@NonNull String filePath) {
        super(resId, args);
        this.filePath = filePath;
    }

    public FileException(@NonNull File file) {
        super(resId, args);
        this.filePath = file.getPath();
    }
}
