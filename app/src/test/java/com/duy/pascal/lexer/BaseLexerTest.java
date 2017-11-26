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

package com.duy.pascal.lexer;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.source.FileScriptSource;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokenizer.GroupParser;
import com.duy.pascal.interperter.tokens.grouping.BaseGrouperToken;
import com.duy.pascal.interpreter.BaseTestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Duy on 12-Jun-17.
 */

public abstract class BaseLexerTest extends BaseTestCase {

    public boolean parse(String fileName) {
        String programPath = dir + fileName;
        ArrayList<ScriptSource> searchPath = new ArrayList<>();
        searchPath.add(new FileScriptSource(new File(programPath)));

        try {
            FileReader fileReader = new FileReader(dir + fileName);
            GroupParser lexer = new GroupParser(new FileScriptSource(fileReader, fileName), searchPath);
            lexer.parse();
            BaseGrouperToken tokenQueue = lexer.getTokenQueue();
            System.out.println(tokenQueue.toCode());
        } catch (GroupingException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean parse(String fileName, String expectResult) {
        String programPath = dir + fileName;
        ArrayList<ScriptSource> searchPath = new ArrayList<>();
        searchPath.add(new FileScriptSource(new File(programPath)));

        try {
            FileReader fileReader = new FileReader(dir + fileName);
            GroupParser lexer = new GroupParser(new FileScriptSource(fileReader, fileName), searchPath);
            lexer.parse();
            System.out.println(lexer.getTokenQueue());
        } catch (GroupingException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
