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

package com.duy.pascal.interperter.declaration;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Duy on 17-Aug-17.
 */

public class Name implements Comparable<Name>, Serializable, Cloneable {
    private String originName;

    private Name(String originName) {
        this.originName = originName;
    }

    public static Name create(String name) {
        return new Name(name);
    }

    public String getOriginName() {
        return originName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return obj.equals(originName);
        }
        return obj == this
                || (obj instanceof Name && ((Name) obj).getOriginName().equalsIgnoreCase(originName))
                || ((obj instanceof CharSequence) && obj.toString().equalsIgnoreCase(originName));
    }

    @Override
    public int compareTo(@NonNull Name o) {
        return originName.toLowerCase().compareTo(o.getOriginName().toLowerCase());
    }

    public int getLength() {
        return originName.length();
    }

    @Override
    public String toString() {
        return originName;
    }


    @Override
    public int hashCode() {
        return originName.toLowerCase().hashCode();
    }
}
