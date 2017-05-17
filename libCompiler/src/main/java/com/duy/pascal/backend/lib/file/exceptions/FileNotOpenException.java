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

package com.duy.pascal.backend.lib.file.exceptions;

import java.io.File;

/**
 * Reported by the following functions : Close, Read, Write, Seek, EOf, FilePos, FileSize, Flush,
 * BlockRead, and BlockWrite if the file is not open.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenException extends FileException {
    public FileNotOpenException(String filePath) {
        super(filePath);
    }

    public FileNotOpenException(File filePath) {
        super(filePath);
    }
}
