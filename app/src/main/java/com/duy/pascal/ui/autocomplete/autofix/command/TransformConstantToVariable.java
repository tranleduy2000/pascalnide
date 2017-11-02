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

package com.duy.pascal.ui.autocomplete.autofix.command;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.exceptions.parsing.value.ChangeValueConstantException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.AutoFixFactory;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.autofix.EditorUtil.getText;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

/**
 * Created by Duy on 11/2/2017.
 */
public class TransformConstantToVariable implements AutoFixCommand {
    private static final String TAG = "TransformConstantToVariable";
    private ChangeValueConstantException e;

    public TransformConstantToVariable(ChangeValueConstantException e) {
        this.e = e;
    }

    @Override
    public void execute(EditorView editable) {
        DLog.d(TAG, "execute() called with: editable = [" + editable + "]");

        LineInfo lineStart = e.getScope().getStartPosition();
        LineInfo lineEnd = e.getLineInfo();
        TextData region = getText(editable, lineStart, lineEnd);
        ConstantAccess constant = e.getConst();

        Pattern pattern = makePattern(constant);
        Matcher matcher = pattern.matcher(region.getText());

        if (matcher.find()) {
            DLog.d(TAG, "changeConstToVar: " + matcher);
            int start = matcher.start(2) + region.getOffset();
            start = Math.max(0, start);
            int end = matcher.end(6) + region.getOffset();

            editable.disableTextWatcher();
            editable.getText().delete(start, end);
            region.getText().delete(matcher.start(2), matcher.end(6));
            editable.enableTextWatcher();

            AutoFixCommand declareVar = AutoFixFactory.declareVar(lineStart, lineEnd,
                    constant.getName(), //name
                    constant.getRuntimeType(null).declType.toString(), //type
                    constant.toCode());
            declareVar.execute(editable);
        } else {
            //const a: integer = 2;
            pattern = Pattern.compile(
                    "(^const\\s+|\\s+const\\s+)" + //1
                            "(" + Pattern.quote(constant.getName() + "") + ")" + //2
                            "(\\s?)" + //3
                            "(:)" + //4
                            "(\\s?)" +//5
                            "(.*?)" +//6 type
                            "(=)" + //7
                            "(.*?)" +//8
                            "(;)" //9
                    , Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

            matcher = pattern.matcher(region.getText());
            if (matcher.find()) {
                int start = matcher.start(2) + region.getOffset();
                start = Math.max(0, start);
                int end = matcher.end(9) + region.getOffset();

                editable.disableTextWatcher();
                editable.getText().delete(start, end);
                region.getText().delete(matcher.start(2), matcher.end(9));
                editable.enableTextWatcher();

                AutoFixCommand declareVar = AutoFixFactory.declareVar(
                        lineStart, lineEnd,
                        constant.getName(),  //name
                        constant.getRuntimeType(null).declType.toString(), //type
                        constant.toCode());
                declareVar.execute(editable);//initialization value
            }
        }
    }

    private Pattern makePattern(ConstantAccess constant) {
        return Pattern.compile(
                "(^const\\s+|\\s+const\\s+)" + //1
                        "(" + Pattern.quote(constant.getName() + "") + ")" + //2 name of consnant
                        "(\\s?)" + //3 space
                        "(=)" +//4
                        "(.*?)" +//5 any type
                        "(;)",//6 semicolon
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    }

    @NonNull
    @Override
    public CharSequence getTitle(Context context) {
        ConstantAccess constant = e.getConst();
        String string = context.getString(R.string.change_const_to_var, constant.getName(),
                constant.getRuntimeType(null).getRawType().toString(), constant.getValue().toString());
        return highlight(context, string);
    }


}
