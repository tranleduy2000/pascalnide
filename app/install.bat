cd app
cp build\outputs\apk\release\app-release.apk app-release.apk
adb uninstall com.duy.pascal.compiler
adb install -r app-release.apk
adb shell am start -n "com.duy.pascal.compiler/com.duy.pascal." -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
exit