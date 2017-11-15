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

package com.duy.pascal.ui.autocomplete.completion;

public enum CompleteContext {
    CONTEXT_USES,
    CONTEXT_VAR,
    CONTEXT_CONST,

    CONTEXT_NEED_TYPE_INTEGER,

    CONTEXT_AFTER_FOR,
    CONTEXT_AFTER_BEGIN,

    CONTEXT_ASSIGN,

    CONTEXT_COMMA_SEMICOLON,
    CONTEXT_AFTER_COLON,
    CONTEXT_TYPE,
    CONTEXT_INSERT_ASSIGN,
    CONTEXT_INSERT_TO,
    CONTEXT_INSERT_DO,
    CONTEXT_NEED_TYPE,
    CONTEXT_DECLARE_KEYWORD,
    CONTEXT_NONE,
}