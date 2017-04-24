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

package com.googlecode.android_scripting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;

import java.lang.reflect.Method;

public abstract class ForegroundService extends Service {
    private static final Class<?>[] mStartForegroundSignature =
            new Class[]{int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[]{boolean.class};

    private final int mNotificationId;

    private NotificationManager mNotificationManager;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];

    public ForegroundService(int id) {
        mNotificationId = id;
    }

    protected abstract Notification createNotification();

    /**
     * This is a wrapper around the new startForeground method, using the older APIs if it is not
     * available.
     */
    private void startForegroundCompat(Notification notification) {
        // If we have the new startForeground API, then use it.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = mNotificationId;
            mStartForegroundArgs[1] = notification;
            try {
                mStartForeground.invoke(this, mStartForegroundArgs);
            } catch (Exception e) {
                Log.e(e);
            }
            return;
        }

        startForeground(0, createNotification());
        if (notification != null) {
            mNotificationManager.notify(mNotificationId, notification);
        }
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older APIs if it is not
     * available.
     */
    private void stopForegroundCompat() {
        // If we have the new stopForeground API, then use it.
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
            try {
                mStopForeground.invoke(this, mStopForegroundArgs);
            } catch (Exception e) {
                Log.e(e);
            }
            return;
        }

        // Fall back on the old API. Note to cancel BEFORE changing the
        // foreground state, since we could be killed at that point.
        mNotificationManager.cancel(mNotificationId);
//        setForeground(false);
        stopForeground(true);
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        try {
            mStartForeground = getClass().getMethod("startForeground", mStartForegroundSignature);
            mStopForeground = getClass().getMethod("stopForeground", mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            // Running on an older platform.
            mStartForeground = mStopForeground = null;
        }
        startForegroundCompat(createNotification());
    }

    @Override
    public void onDestroy() {
        // Make sure our notification is gone.
        stopForegroundCompat();
    }
}
