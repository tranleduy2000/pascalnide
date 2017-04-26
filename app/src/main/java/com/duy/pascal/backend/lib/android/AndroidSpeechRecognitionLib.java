/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duy.pascal.backend.lib.android;

import android.content.Intent;
import android.speech.RecognizerIntent;

import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.googlecode.sl4a.rpc.PascalParameter;

import java.util.ArrayList;

/**
 * A facade containing RPC implementations related to the speech-to-text functionality of Android.
 *
 * @author Felix Arends (felix.arends@gmail.com)
 */
public class AndroidSpeechRecognitionLib extends BaseAndroidLibrary {
    public static final String NAME = "aSpeechRecord".toLowerCase();
    private final AndroidUtilsLib mAndroidFacade;

    public AndroidSpeechRecognitionLib(AndroidLibraryManager manager) {
        super(manager);
        mAndroidFacade = new AndroidUtilsLib(manager);
    }

    @PascalMethod(description = "Recognizes user's speech and returns the most likely result.", returns = "An empty string in case the speech cannot be recongnized.")
    public StringBuilder speechToText(
            @PascalParameter(name = "prompt", description = "text prompt to show to the user when asking them to speak")
            final String prompt,
            @PascalParameter(name = "language", description = "language override to inform the recognizer that it should expect speech in a language different than the one set in the java.util.Locale.getDefault()")
            final String language,
            @PascalParameter(name = "languageModel", description = "informs the recognizer which speech model to prefer (see android.speech.RecognizeIntent)")
            final String languageModel) {
        final Intent recognitionIntent =
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Setup intent parameters (if provided).
        if (language != null) {
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "");
        }
        if (languageModel != null) {
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "");
        }
        if (prompt != null) {
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
        }

        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(recognitionIntent);

        if (data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
            // The result consists of an array-list containing one entry for each
            // possible result. The most likely result is the first entry.
            ArrayList<String> results =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            return new StringBuilder(results.get(0));
        }

        return new StringBuilder("");
    }

    @PascalMethod(description = "Recognizes user's speech and returns the most likely result.", returns = "An empty string in case the speech cannot be recongnized.")
    public StringBuilder speechToText() {
        final Intent recognitionIntent =
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Run the activity an retrieve the result.
        final Intent data = mAndroidFacade.startActivityForResult(recognitionIntent);

        if (data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
            // The result consists of an array-list containing one entry for each
            // possible result. The most likely result is the first entry.
            ArrayList<String> results =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            return new StringBuilder(results.get(0));
        }
        return new StringBuilder("");
    }

    @Override
    public void shutdown() {
    }
}
