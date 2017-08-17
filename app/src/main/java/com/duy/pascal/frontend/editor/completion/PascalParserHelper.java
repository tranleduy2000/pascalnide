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

package com.duy.pascal.frontend.editor.completion;

import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import java.io.StringReader;

/**
 * Created by Duy on 17-Aug-17.
 */

public class PascalParserHelper {
    public void parse(String source, int cursor) {
        try {
            PascalProgramDeclaration pascalProgram =
                    PascalCompiler.loadPascal("", new StringReader(source), null, null);
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }
}
