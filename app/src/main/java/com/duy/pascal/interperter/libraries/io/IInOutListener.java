/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.libraries.io;

/**
 * Created by Duy on 21-Apr-17.
 */

public interface IInOutListener {
    void startInput(IOLib lock);

    void print(CharSequence charSequence);

    void println(CharSequence charSequence);

    char getKeyBuffer();

    boolean keyPressed();

    String getCurrentDirectory();
}
