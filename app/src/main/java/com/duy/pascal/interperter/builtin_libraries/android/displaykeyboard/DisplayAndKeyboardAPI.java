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

package com.duy.pascal.interperter.builtin_libraries.android.displaykeyboard;

import android.content.pm.ActivityInfo;

import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;

/**
 * Created by Duy on 18-Jun-17.
 */

@SuppressWarnings("unused")
public class DisplayAndKeyboardAPI extends PascalLibrary {
    public static final String NAME = "aKeyboard".toLowerCase();
    private ProgramHandler handler;
    private int orientation = Orientation.LANDSCAPE;

    public DisplayAndKeyboardAPI() {

    }

    public DisplayAndKeyboardAPI(ProgramHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public Integer getDisplayMode() {
        return 0;
    }

    public void setDisplayMode(Integer mode) {
        switch (mode) {
            case ActivityMode.FULL_SCREEN:
                break;
            case ActivityMode.NO_ACTION_BAR:
                break;
            case ActivityMode.NO_LIMIT:
                break;
            case ActivityMode.STANDARD:
                break;
        }
    }

    public int getCurrentOrientation() {
        return orientation;
    }

    public void ensureActivityNonNull() {
        if (handler.getActivity() == null) {
            throw new RuntimeException();
        }
    }

    public void setOrientation(Integer orientation) {
        ensureActivityNonNull();
        this.orientation = orientation;
        switch (orientation) {
            case Orientation.LANDSCAPE:
                handler.getActivity()
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Orientation.PORTRAIT:
                handler.getActivity()
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    public void showKeyboard() {

    }

    public void hideKeyboard() {

    }

    public Boolean isKeyboardShowing() {
        return false;
    }
}
