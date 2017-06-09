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

import java.util.regex.Pattern;

/**
 * Created by Duy on 11-Feb-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Patterns {

    //Words
    public static final Pattern LINE = Pattern.compile(".*\\n");

    /**
     * match number
     */
    public static final Pattern NUMBERS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");

    /**
     * match reserved keyword
     */
    public static final Pattern KEYWORDS = Pattern.compile(
            "\\b(uses|const|do|for|while|if|else|in|case|and|array|begin|div" +
                    "|downto|to|mod|of" +
                    "|procedure|program|repeat|until|shl|shr" +
                    "|then|type|var|end|function" +
                    "|true|false" +
                    "|and|or|xor|not|break|exit" +
                    "|integer|byte|word|shortint|smallint|cardinal" +
                    "|string|ansistring" +
                    "|single|real|extended|comp|curreny" +
                    "|longint|int64|qword|longword|dword" +
                    "|boolean" +
                    "|char|text" +
                    "|record|continue" +
                    "|unit|interface|initialization|finalization|implementation|" +
                    "null|nil|set|new)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * match builtin pascal function
     */
    public static final Pattern BUILTIN_FUNCTIONS = Pattern.compile(
            "\\b(sin|cos|sqrt|length" +
                    "|exp|tan|keyPressed|readKey|delay|random|randomize|inc|dec" +
                    "|ceil|trunc|frac|floor|abs|round|sqr|pred|succ|ln|arctan" +
                    "|odd|int|halt|odd)\\b", Pattern.CASE_INSENSITIVE);

    /**
     * match comment, include // { } (* *) comment
     */
    public static final Pattern COMMENTS = Pattern.compile(
            "(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)" + //splash splash comment
                    "|(\\{(?:.|[\\n\\r])*?\\})" + //{ } comment
                    "|((\\(\\*)(?:.|[\\n\\r])*?(\\*\\)))"// (* *) comment
    );

    /**
     * match some spacial symbol
     */
    public static final Pattern SYMBOLS = Pattern.compile("[+\\-'*=<>/:)(\\]\\[;@\\^,.]");

    /**
     * match string pascal
     * include
     * <p>
     * 'string'
     * <p>
     * And can not find close quote
     * <p>
     * 'sadhasdhasdhashdhas ds asda sd
     */
    public static final Pattern STRINGS = Pattern.compile(
            "((')(.*?)('))" +//'string'
                    "|((')(.*+))", Pattern.DOTALL); // no end string 'asdasdasd

    public static final Pattern REPLACE_HIGHLIGHT = Pattern.compile("\"(.*?)\"");

    public static final Pattern REPLACE_CURSOR = Pattern.compile("%\\w");

    public static final Pattern VAR = Pattern.compile("\\b(var)\\b", Pattern.CASE_INSENSITIVE);
    public static final Pattern TYPE = Pattern.compile("\\b(type)\\b", Pattern.CASE_INSENSITIVE);
    public static final Pattern PROGRAM = Pattern.compile("\\b(program)[\\s](.*?);\\b", Pattern.CASE_INSENSITIVE);
    public static final Pattern USES = Pattern.compile("\\b(uses)[\\s](.*?);\\b", Pattern.CASE_INSENSITIVE);
    public static final Pattern CONST = Pattern.compile("\\b(const)\\b", Pattern.CASE_INSENSITIVE);

    public static final Pattern OPEN_PATTERN
            = Pattern.compile("(begin|then|else|do|repeat|of|" +
                    "type|var|const|interface|implementation)",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static final Pattern END_PATTERN
            = Pattern.compile("\\b(end)\\b", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

}
