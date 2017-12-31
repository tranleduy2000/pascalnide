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

package com.duy.pascal.interperter.exceptions.parsing.value;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.value.CharacterToken;

import android.support.annotation.NonNull;


public class NonCharValueException extends ParsingException {
    @NonNull
    private CharacterToken characterToken;

    public NonCharValueException(@NonNull LineInfo line, @NonNull CharacterToken characterToken) {
        super(line);
        this.characterToken = characterToken;
        this.setLineInfo(line);
    }

    @NonNull
    public final CharacterToken getCharacterToken() {
        return this.characterToken;
    }

    public final void setCharacterToken(@NonNull CharacterToken var1) {
        this.characterToken = var1;
    }
}