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

package com.duy.pascal.backend.exceptions.define;

import com.js.interpreter.NamedEntity;

public class SameNameException extends com.duy.pascal.backend.exceptions.ParsingException {
    public String type, name;
    public String preType, preLine;

    public SameNameException(NamedEntity previous, NamedEntity current) {
        super(current.getLineNumber(), current.getEntityType() + " " + current.name()
                + " conflicts with previously defined "
                + previous.getEntityType() + " with the same name defined at "
                + previous.getLineNumber());
        this.type = current.getEntityType();
        this.name = current.name();
        this.preType = previous.getEntityType();
        this.preLine = previous.getLineNumber().toString();
    }
}
