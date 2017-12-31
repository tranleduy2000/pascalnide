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

package com.duy.pascal.interperter.exceptions.parsing.io;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public class InputStreamNotFoundException extends RuntimePascalException {
    public InputStreamNotFoundException(@NonNull String message) {
        super(null, message);
    }

    public InputStreamNotFoundException() {
        super("InputStreamNotFoundException");
    }
}
