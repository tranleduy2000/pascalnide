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

package com.duy.pascal.frontend.code_completion;

import java.util.regex.Pattern;

/**
 * Created by Duy on 11-Feb-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Patterns {

    //Words
    public static final Pattern line = Pattern.compile(".*\\n");
    public static final Pattern NUMBERS = Pattern.compile(
            "\\b(\\d*[.]?\\d+)\\b");
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
                    "|record" +
                    "|unit|interface|initialization|finalization|implemention)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern FUNCTIONS = Pattern.compile(
            "\\b(sin|cos|sqrt|length" +
                    "|exp|tan|keyPressed|readKey|delay|random|randomize|inc|dec" +
                    "|ceil|trunc|frac|floor|abs|round|sqr|pred|succ|ln|arctan" +
                    "|odd|int|halt|odd)\\b", Pattern.CASE_INSENSITIVE);

    public static final Pattern COMMENTS = Pattern.compile(
            "(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)" +
                    "|(\\{(?:.|[\\n\\r])*?\\})" +
                    "|((\\(\\*)(?:.|[\\n\\r])*?(\\*\\)))");

    public static final Pattern SYMBOLS = Pattern.compile("[+\\-'*=<>/:)(\\]\\[;]");

    public static final Pattern STRINGS = Pattern.compile("('(.*?)')|('(.*?)[\\r\\n]+)");


}
