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

package com.duy.pascal.interperter.exceptions.parsing;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;


public class PermissionDeniedException extends ParsingException {
    @NonNull
    private String libName;
    @NonNull
    private String permission;
    @NonNull
    private LineNumber line;

    public PermissionDeniedException(@NonNull String libName, @NonNull String permission, @NonNull LineNumber line) {
        super(line, "Permission " + permission + " denied");
        this.libName = libName;
        this.permission = permission;
        this.line = line;
    }

    @NonNull
    public final String getLibName() {
        return this.libName;
    }

    public final void setLibName(@NonNull String var1) {
        this.libName = var1;
    }

    @NonNull
    public final String getPermission() {
        return this.permission;
    }

    public final void setPermission(@NonNull String var1) {
        this.permission = var1;
    }

    @NonNull
    public final LineNumber getLine() {
        return this.line;
    }

    public final void setLine(@NonNull LineNumber var1) {
        this.line = var1;
    }
}
