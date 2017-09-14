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

package com.duy.pascal.frontend.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

/**
 * update 1/9/2017
 */
public class StoreUtil {
    public static void gotoPlayStore(Activity context, String APP_ID) {
        Uri uri = Uri.parse("market://details?id=" + APP_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Uri link = Uri.parse("http://play.google.com/store/apps/details?id=" + APP_ID);
            Intent intent = new Intent(Intent.ACTION_VIEW, link);
            context.startActivity(intent);
        }
    }

    public static void gotoPlayStore(Activity context, String APP_ID, int request) {
        Uri uri = Uri.parse("market://details?id=" + APP_ID);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Uri link = Uri.parse("http://play.google.com/store/apps/details?id=" + APP_ID);
            Intent intent = new Intent(Intent.ACTION_VIEW, link);
            context.startActivityForResult(intent, request);
        }
    }

    public static void shareThisApp(Activity context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" +
                context.getPackageName());
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public static void moreApp(Activity mainActivity) {
        String location = "https://play.google.com/store/apps/dev?id=7055567654109499514";
        Uri uri = Uri.parse(location);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            mainActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }
}
