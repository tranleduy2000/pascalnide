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

package com.duy.pascal.interperter.config;

/**
 * Created by Duy on 14-Jun-17.
 */

public class ProgramConfig {
    private static final String MODE = "$MODE";
    private boolean library;
    private byte mode = ProgramMode.FPC;

    public ProgramConfig() {
    }

    public void process(String[] command) {
        if (command.length == 2) {
            if (command[0].equals(MODE)) {
                switch (command[2]) {
                    case "DELPHI":
                        setMode(ProgramMode.DELPHI);
                        break;
                    case "FPC":
                        setMode(ProgramMode.FPC);
                        break;
                    case "ANDROID":
                        setMode(ProgramMode.ANDROID);
                        break;
                }
            }
        }
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public boolean isLibrary() {
        return library;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }


}
