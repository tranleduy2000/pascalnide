#!/usr/bin/env bash
cd app
//uninstall command
adb uninstall com.duy.pascal.compiler

//install command
adb install -r app-prod-release.apk

//start command
adb shell am start -n "com.duy.pascal.compiler/com.duy.pascal.frontend.activities.ActivitySplashScreen" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER