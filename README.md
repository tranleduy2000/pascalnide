# Pascal NIDE - Pascal Compiler for Android

<img src="https://github.com/tranleduy2000/pascalnide/blob/master/art/wall2..png">

See app on Google Play Store

<a href="https://play.google.com/store/apps/details?id=com.duy.pascal.compiler">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width = 200/>
</a>

# Overview

The application is a Pascal interpreter on Android. This application is confusing for everyone to learn Pascal on the mobile without a computer, so that we can practice anytime, anywhere.

# The main features of the IDE:

  1. Compile Pascal programs and run them without Internet.
  2. Report error when compiling
  3. Powerful editor with many smart features: 
	    * File menu: create a new program file, open, save,automatically save file
	    * Menu edit: Undo, redo, copy, paste.
	    * Auto suggest: Display a small popup window that suggests words that coincide with the word being typed
	    * Auto format: automatically reformat the code for easier viewing.
	    * Find / Find and replace: Regular Expression support.
	    * Goto line: Move the cursor to a line.
	    * Highlight code: highlight the keywords.
	    * Code style: many interface for the editor.
	    * Font size, font, word wrap.
  4. Support library Android (Sensor, battery, record audio, camera...)
	
# Features that the interpreter is missing

- Pointers
- Exceptions
- Set, and Variant types
- for ... in ... do loops
- with ... do statements
- goto, label (it will never supported)
	
# Libraries supported for Android

- The **aTTSpeech** library converts text to speech (requires TextToSpeech). See example `text_to_speech.pas`
- The **aRecognition** library converts speech to text (requires Google Voice). See example `speech_to_text.pas`.
- The **aVibrate** library supports vibration control. You see example `vibrate.pas`
- The **aSensor** library supports processing of Android sensors (light, acceleration, ...). See `accelerometer_sensor.pas` for accelerometer sensor example
- The **aNotify** library helps display notifications in the status bar. See the example `notify.pas`
- The **aClipboard** library works with the clipboard in Android. See the `clipboard.pas` example
- The **aBattery** library retrieves the battery information of the device. See `battery.pas` example
- The **aMedia** library Provides classes that manage various media interfaces in audio and video. See https://github.com/tranleduy2000/pascalnide/blob/master/wiki/aMedia_library.md
- **aTone** library provides API to play DTMF tones (ITU-T Recommendation Q.23). See https://github.com/tranleduy2000/pascalnide/blob/master/wiki/aTone_library.md

# How to contribute

All contributions are welcome, from code to documentation to graphics to design suggestions to bug reports. Please use GitHub to its fullest-- contribute Pull Requests, contribute tutorials or other wiki content-- whatever you have to offer, we can use it!

# Tutorials

See in https://pascalnide.wordpress.com/
  1. <a href="https://pascalnide.wordpress.com/2017/05/01/tao-ung-dung-giao-tiep-bang-giong-noi-voi-pascal-n-ide/">How to create communication application by voice with Pascal N-IDE></a>
  
# License
	The code in this repository is licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
**NOTE**: This software depends on other packages that may be licensed under different open source licenses.

# Developer 
 Trần Lê Duy
 
# Third party library
   * JSPIIJ https://github.com/jeremysalwen/JSPIIJ
   * SL4A https://github.com/damonkohler/sl4a
   * And more...
