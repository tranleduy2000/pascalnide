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

package com.duy.pascal.ui.themefont.themes.database;

/**
 * Created by Duy on 11/3/2017.
 */

public class CodeThemeContract {
    public static class CodeThemeEntry {
        public static final String TABLE_NAME = "tbl_theme";
        public static final String NAME = "theme_name";
        public static final String BACKGROUND = "background_color";
        public static final String NORMAL = "normal_text_color";
        public static final String KEY_WORD = "key_word_color";
        public static final String BOOLEAN = "boolean_color";
        public static final String ERROR = "error_color";
        public static final String NUMBER = "number_color";
        public static final String OPERATOR = "opt_color";
        public static final String COMMENT = "comment_color";
        public static final String STRING = "string_color";

    }
}
