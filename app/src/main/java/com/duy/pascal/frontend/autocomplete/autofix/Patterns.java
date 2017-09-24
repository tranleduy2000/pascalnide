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

package com.duy.pascal.frontend.autocomplete.autofix;

import java.util.regex.Pattern;

/**
 * Created by Duy on 9/24/2017.
 */

public class Patterns {
    public static final Pattern VAR = Pattern.compile("(\\s(var)|^(var))\\s", Pattern.CASE_INSENSITIVE);
    public static final Pattern TYPE = Pattern.compile("(\\s(type)|^(type))\\s", Pattern.CASE_INSENSITIVE);
    public static final Pattern PROGRAM = Pattern.compile("(\\W(program)|^(program))[\\s](.*?);", Pattern.CASE_INSENSITIVE);
    public static final Pattern USES = Pattern.compile("(\\s(uses)|^(uses))[\\s](.*?);", Pattern.CASE_INSENSITIVE);
    public static final Pattern CONST = Pattern.compile("(\\s(const)|^(const))\\s", Pattern.CASE_INSENSITIVE);

    public static final Pattern REPLACE_HIGHLIGHT = Pattern.compile("\"(.*?)\"");
    public static final Pattern REPLACE_CURSOR = Pattern.compile("%\\w");

}
