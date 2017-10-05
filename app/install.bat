cd app
adb uninstall com.duy.pascal.compiler
adb install -r app-prod-release.apk
adb shell am start -n "com.duy.pascal.compiler/com.duy.pascal.ui.activities.ActivitySplashScreen" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER