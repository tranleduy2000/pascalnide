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

package com.duy.pascal.backend.lib.android;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.rpc.RpcDefault;

import java.util.Map;

/**
 * Generate DTMF tones.
 */
public class AndroidToneGeneratorLib implements PascalLibrary {

    public static final String NAME = "aTone".toLowerCase();
    private final ToneGenerator mToneGenerator;

    public AndroidToneGeneratorLib(AndroidLibraryManager manager) {
        mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    public static void playSound(double frequency, int duration) {
        try {
            // AudioTrack definition
            int mBufferSize = AudioTrack.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_8BIT);

            AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    mBufferSize, AudioTrack.MODE_STREAM);

            // Sine wave
            double[] mSound = new double[4410];
            short[] mBuffer = new short[duration];
            for (int i = 0; i < mSound.length; i++) {
                mSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / frequency)));
                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
            }

            mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
            mAudioTrack.play();

            mAudioTrack.write(mBuffer, 0, mSound.length);
            mAudioTrack.stop();
            mAudioTrack.release();
        } catch (Exception ignored) {

        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Generate DTMF tones for the given phone number.")
    public void generateTones(
            @PascalParameter(name = "phoneNumber") String phoneNumber,
            @PascalParameter(name = "toneDuration", description = "duration of each tone in milliseconds")
            @RpcDefault("100") int toneDuration)
            throws InterruptedException {
        try {
            for (int i = 0; i < phoneNumber.length(); i++) {
                switch (phoneNumber.charAt(i)) {
                    case '0':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0);
                        Thread.sleep(toneDuration);
                        break;
                    case '1':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_1);
                        Thread.sleep(toneDuration);
                        break;
                    case '2':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_2);
                        Thread.sleep(toneDuration);
                        break;
                    case '3':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_3);
                        Thread.sleep(toneDuration);
                        break;
                    case '4':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_4);
                        Thread.sleep(toneDuration);
                        break;
                    case '5':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_5);
                        Thread.sleep(toneDuration);
                        break;
                    case '6':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_6);
                        Thread.sleep(toneDuration);
                        break;
                    case '7':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_7);
                        Thread.sleep(toneDuration);
                        break;
                    case '8':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_8);
                        Thread.sleep(toneDuration);
                        break;
                    case '9':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_9);
                        Thread.sleep(toneDuration);
                        break;
                    case '*':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_S);
                        Thread.sleep(toneDuration);
                        break;
                    case '#':
                        mToneGenerator.startTone(ToneGenerator.TONE_DTMF_P);
                        Thread.sleep(toneDuration);
                        break;
                    default:
                        throw new RuntimeException("Cannot generate tone for '" + phoneNumber.charAt(i) + "'");
                }
            }
        } finally {
            mToneGenerator.stopTone();
        }
    }

    @PascalMethod(description = "Generate and play a sound with frequency in duration (miliseconds)")
    public void generateSound(int frequency, int duration) {
        playSound(frequency, duration);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")
    public void shutdown() {
    }

}
