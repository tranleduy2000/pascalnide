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

package com.duy.pascal.backend.runtime.references;

import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

import java.util.LinkedList;

/**
 * Created by Duy on 25-May-17.
 */

public class SetPreference implements Reference {
    private LinkedList array;
    private int index;

    public SetPreference(LinkedList array, int index) {
        this.array = array;
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public void set(Object value) {
        array.set(index, value);
    }

    public Object get() throws RuntimePascalException {
        return array.get(index);
    }

    @Override
    public SetPreference clone() {
        return null;
    }
}
