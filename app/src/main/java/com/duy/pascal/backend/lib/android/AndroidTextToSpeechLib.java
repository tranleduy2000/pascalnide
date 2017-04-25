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

import android.os.Build;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.googlecode.sl4a.rpc.PascalParameter;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * Provides Text To Speech services for API 4 or more.
 */

public class AndroidTextToSpeechLib extends BaseAndroidLibrary {
    public static final String NAME = "aSpeech";

    private final TextToSpeech mTextToSpeech;
    private final CountDownLatch mOnInitLock;

    public AndroidTextToSpeechLib(AndroidLibraryManager manager) {
        super(manager);
        mOnInitLock = new CountDownLatch(1);
        mTextToSpeech = new TextToSpeech(manager.getContext(), new OnInitListener() {
            @Override
            public void onInit(int arg0) {
                mOnInitLock.countDown();
            }
        });
    }

    @Override
    public void shutdown() {
        while (mTextToSpeech.isSpeaking()) {
            SystemClock.sleep(100);
        }
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Speaks the provided message via TTS.")
    public void speak(@PascalParameter(name = "message") StringBuilder message) throws InterruptedException {
        mOnInitLock.await();
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String utteranceId = this.hashCode() + "";
                mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else {
                mTextToSpeech.speak(message.toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns True if speech is currently in progress.")
    public boolean isSpeaking() throws InterruptedException {
        mOnInitLock.await();
        return mTextToSpeech.isSpeaking();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a Locale instance describing the language.")
    public StringBuilder getLanguage() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return new StringBuilder(mTextToSpeech.getVoice().getLocale().toString());
        } else {
            return new StringBuilder(mTextToSpeech.getLanguage().toString());
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Set language for speak.")
    public void setLanguage(StringBuilder code) {
        mTextToSpeech.setLanguage(new Locale(code.toString()));
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the speech pitch for the TextToSpeech engine.")
    public void setPitch(double d) {
        mTextToSpeech.setPitch((float) d);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Sets the speech rate.")
    public void setSpeechRate(double rate) {
        mTextToSpeech.setSpeechRate((float) rate);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Stop the speak")
    public void stopSpeak() {
        if (mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Stop the speak")
    public void setDefaultLanguage() {
        mTextToSpeech.setLanguage(Locale.getDefault());
    }
}
