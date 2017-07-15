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

package com.duy.pascal.interperter.builtin_libraries.android.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.android.exceptions.MediaFileNotAssignException;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalParameter;
import com.duy.pascal.interperter.builtin_libraries.file.exceptions.FileNotFoundException;
import com.duy.pascal.frontend.DLog;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class AndroidMediaPlayerLib implements PascalLibrary {

    public static final String NAME = "aMedia".toLowerCase();
    private static final String TAG = "AndroidMediaPlayerLib";
    private final Map<String, MediaPlayer> mPlayers = new Hashtable<>();
    private final Map<String, String> mUrls = new Hashtable<>();
    private final Context mContext;


    public AndroidMediaPlayerLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
    }

    private MediaPlayer getPlayer(String key) {
        return mPlayers.get(key);
    }

    private String getUrl(String key) {
        return mUrls.get(key);
    }

    @PascalMethod(description = "Return url of media")
    public StringBuilder getMediaUrl(String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        return new StringBuilder(mUrls.get(key));
    }

    private void push(String key, MediaPlayer player, String url) {
        mPlayers.put(key, player);
        mUrls.put(key, url);
    }

    private void remove(String key) {
        MediaPlayer player = mPlayers.get(key);
        if (player != null) {
            player.stop();
            player.release();
        }
        mPlayers.remove(key);
        mUrls.remove(key);
    }

    @PascalMethod(description = "Open a media file", returns = "true if play successful")
    public boolean assignMedia(
            @PascalParameter(name = "url", description = "url of media resource") String url,
            @PascalParameter(name = "key", description = "identifying resource") String key)
            throws FileNotFoundException {
        remove(key);
        MediaPlayer player;
        try {
            Uri parse = Uri.parse(url);
            if (parse == null) {
                throw new FileNotFoundException(url);
            }
            player = MediaPlayer.create(mContext, parse);
        } catch (Exception e) {
            throw new FileNotFoundException(url);
        }
        if (player != null)
            push(key, player, url);

        return player != null;
    }

    @PascalMethod(description = "Close media file", returns = "true if successful")
    public boolean closeMedia(
            @PascalParameter(name = "key", description = "string identifying resource") String key)
            throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        remove(key);
        return true;
    }

    @PascalMethod(description = "Play a media with key")
    public void playMedia(String key) throws MediaFileNotAssignException {
        DLog.d(TAG, "playMedia() called with: key = [" + key + "]");

        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        if (!player.isPlaying()) {
            player.start();
        }
        player.start();
    }

    @PascalMethod(description = "Pause media")
    public void pauseMedia(String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        if (player.isPlaying()) {
            player.pause();
        }
        player.start();
    }


    @PascalMethod(description = "Checks if media file is playing.", returns = "true if playing")
    public boolean isMediaPlaying(
            @PascalParameter(name = "key", description = "string identifying resource") String key)
            throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        return player != null && player.isPlaying();
    }


    @PascalMethod(description = "Gets the current playback position.")
    public int getMediaCurrentPosition(String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        return player.getCurrentPosition();
    }


    @PascalMethod(description = "Checks whether the MediaPlayer is looping or non-looping.")
    public boolean isMediaLooping(String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        return player.isLooping();
    }

    private void assertPlayerNonNull(String key) throws MediaFileNotAssignException {
        if (getPlayer(key) == null)
            throw new MediaFileNotAssignException(key);
    }


    @PascalMethod(description = "Set Looping", returns = "True if successful")
    public boolean setMediaLoop(
            @PascalParameter(name = "enabled") boolean enabled, String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        if (player == null) {
            return false;
        }
        player.setLooping(enabled);
        return true;
    }


    @PascalMethod(description = "Moves the media to specified time position", returns = "New Position (in ms)")
    public void mediaSeekTo(
            @PascalParameter(name = "msec", description = "Position in millseconds") int msec,
            @PascalParameter(name = "key", description = "string identifying resource") String key) throws MediaFileNotAssignException {
        assertPlayerNonNull(key);
        MediaPlayer player = getPlayer(key);
        player.seekTo(msec);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "Stop all media before exit program")
    public void shutdown() {
        Set<Entry<String, MediaPlayer>> entries = mPlayers.entrySet();
        for (Entry<String, MediaPlayer> entry : entries) {
            MediaPlayer player = entry.getValue();
            if (player != null) {
                player.stop();
                player.release();
            }
        }
        mPlayers.clear();
        mUrls.clear();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
