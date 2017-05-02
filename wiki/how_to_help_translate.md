# How to localize Pascal N-IDE for Android
## Pascal N-IDE for Android has been localized for these locales:
    •	English <-- this is the default.
    •	Việt Nam
    •	Czech (cs)
    •	Romanian (ro)
    •	Russian (ru)
    •	Spanish (es)
    •	Slovak (sk)
All of these translations (besides English) have been provided by volunteers. More translations and improved translations are gratefully accepted.
You don't have to be a programmer to create the translations, all you need is a text editor, some knowledge of technical English, and of course knowledge of the technical version of your own language.

## How to improve a localization or create a brand-new localization

**Use a text editor that supports UTF-8**

Android resource files are encoded using the UTF-8 character set. If possible, please use a text editor that can read and write UTF-8 encoded files to edit the resource files. For example, on Windows you should use an editor like the free (Notepad++)[http://notepad-plus-plus.org/] editor. Or use "Turbo editor" in Android.

###To improve an existing localization

You'll need both the English version of the files as well as the existing localized versions of the files.

1. Download these two files (they are the U.S. English version of the UI:
    https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values/msg_error.xml
    https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values/strings.xml
2. Download the corresponding two files for your locale. (instead of XX use your locale's locale code).
     https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values-XX/msg_error.xml
     https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values-XX/strings.xml
3. Edit the localized files to edit string strings from the original English files.
4. Email the localized copies of those files to tranleduy1233@gmail.com

### Creating a brand new localization

To contribute a new localization for the Terminal Emulator for Android, please:

1.	Download these two files (they are the U.S. English version of the UI:
    https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values/msg_error.xml
    https://github.com/tranleduy2000/pascalnide/blob/master/app/src/main/res/values/strings.xml
2.	Edit those two files to change the English strings to your language strings.
3.	Email the localized copies of those files to tranleduy1233@gmail.com

## Example format

    <resources>
        <string name="deleted">Deleted</string>
        <string name="failed">Failed</string>
    </resource>

Here is the same section of the file, "localized" by being changed to "Tiếng Việt":

    <resources>
       <string name="deleted">Đã xóa</string>
       <string name="failed">Thất bại</string>
    </resource>




