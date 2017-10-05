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

package com.duy.pascal.ui.autocomplete.autofix.command;

/**
 * Created by Duy on 9/24/2017.
 */

public class CommandDescriptor {
    private CharSequence title;
    private AutoFixCommand command;

    public CommandDescriptor(CharSequence title, AutoFixCommand command) {
        this.title = title;
        this.command = command;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public AutoFixCommand getCommand() {
        return command;
    }

    public void setCommand(AutoFixCommand command) {
        this.command = command;
    }
}

