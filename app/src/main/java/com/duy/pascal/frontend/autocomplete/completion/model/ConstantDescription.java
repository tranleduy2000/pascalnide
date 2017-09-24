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

package com.duy.pascal.frontend.autocomplete.completion.model;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;

/**
 * Created by Duy on 9/24/2017.
 */

public class ConstantDescription extends DescriptionImpl {
    private final Object value;

    public ConstantDescription(@NonNull Name name, String description, Type type, Object value) {
        super(KIND_CONST, name, description, type);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public ConstantDescription(ConstantDefinition c) {
        super(KIND_CONST, c.getName(), c.getDescription(), c.getType());
        this.name = name;
        this.type = type;
        this.value = c.getValue();
    }

    @Override
    public String getHeader() {
        if (type == null) {
            return name + "=" + value;
        } else {
            return name + ":" + type + "=" + value;
        }
    }
}
