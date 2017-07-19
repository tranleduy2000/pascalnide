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

package com.duy.pascal.interperter.ast.runtime_value.references;

import java.lang.reflect.Array;

public class ArrayPointer<T> implements PascalReference<T> {
    private final int index;
    private final Object container;

    boolean isString;

    public ArrayPointer(Object container, int index) {
        this.container = container;
        this.index = index;
        isString = container instanceof StringBuilder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (isString) {
            return (T) (Character) ((StringBuilder) container).charAt(index - 1);
        } else {
            return (T) Array.get(container, index);
        }
    }

    @Override
    public void set(T value) {
        if (isString) {
            ((StringBuilder) container).setCharAt(index, (Character) value);
        } else {
            Array.set(container, index, value);
        }
    }

    @Override
    public ArrayPointer<T> clone() {
        return new ArrayPointer<>(container, index);
    }

    @Override
    public String toString() {
        return "^" + container.toString();
    }
}
