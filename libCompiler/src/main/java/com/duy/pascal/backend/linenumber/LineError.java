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

package com.duy.pascal.backend.linenumber;

/**
 * Created by Duy on 13-May-17.
 */

public class LineError extends LineInfo {
    /**
     * The length of a token is error;
     * for example, the variable "variable" has not been declared,
     * <code>offset = "variable".length()</code>;
     * <p>
     * default value is -1
     */
    private int offset = -1;

    public LineError(int line, String sourceFile) {
        super(line, sourceFile);
    }

    public LineError(int line, int column, String sourceFile) {
        super(line, column, sourceFile);
    }

    public LineError(int line, int column, int offset, String sourceFile) {
        super(line, column, sourceFile);
        this.offset = offset;
    }

    public LineError(LineInfo lineInfo, int offset) {
        super(lineInfo.getLine(), lineInfo.getColumn(), lineInfo.getSourceFile());
        this.offset = offset;
    }


    public int getOffset() {
        return offset;
    }
}
