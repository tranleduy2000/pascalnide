*****Release**:	1.2 beta
Feb 27, 9:35 PM:

    Fix crash Support "clrscr"
    Auto remove "uses crt"

***
**Release**:	1.3 beta
Feb 28, 8:09 PM: 

    clrscr, whereX, whereY, gotoXY, delay, textColor, textBackground

***
**Release**:	1.4 beta
Mar 1, 6:22 PM:

    Fixes some bug
    Add some futures
    Add procedure
    GetTime(a, b, c, d);
    GetDate(a, b, c, d);
    str(number, string)
    val(string, number, code_result)

***
**Release**:	1.8 beta
Mar 5, 5:35 PM:

    Improve smart editor.
    Crash fixes when readln;

***
**Release**:	2.3 beta
Mar 8, 2:47 PM:

    fix random
    add some function: inc(), dec(), arctan(), ln(), int()
***
**Release**:	2.4 beta
Mar 9, 5:44 PM: 

    You can then set value to a function by the function name without using the "result" keyword
    New function concat(string...)
    Fixed some bug.
***
**Release**:	2.5 beta
Mar 11, 9:45 PM: 

    Supported file, files must be placed in the directory of the application sdcard/PascalCompiler/
    read some changes carefully:
    writef (file, var); Write to the variable var... to file
    writelnf (file, var); Write to the variable var ... to file and write new line
    readf (file, var); Read the variable var, read only 1 variable
    readlnf (file, var); Read the variable var and go to next line, read only 1 variable
    Supported function keypressed
***
**Release**:	2.7 beta
Mar 13, 11:34 PM:

    Update theme fix some bug.
***
**Release**:	3.4.0
Mar 18, 9:25 PM:

    Add some fonts
    Change file view
    Fix bug compare string
    Unlock undo redo feature
***
**Release**:	3.4.1
Mar 24, 11:53 AM: 

    New theme Modnokai Coffee
    Supported uses token
    Supported comment in code, include: {} (* *) // /* */
    Fix bug in feature "go to line"
    Fix some bug

***
**Release**:	3.4.2
Mar 25, 8:03 PM:

    Function "byte"
***
**Release**:	3.4.3
Mar 27, 3:31 PM: 

    Tab button
    Function readkey
    Added "break", "exit" command Function "byte"
***
**Release**:	3.4.4
Mar 28, 1:46 PM:

    Translate to Russia
    Fix bug case..of.. statement
***
**Release**:	3.4.6

    Fix bug keyboard enter and delete
***
**Release**:	3.4.8
Mar 31, 10:35 PM: 

    Fix bug function (var arg....)
    Fix bug if statement.
    Check type in case statement
    Support some procedure of graph lib
    Add action remove file
    Supported setting background, text color of console, max buffer, frame rate.
    Fix lag when write code in editor

***
**Release**:	3.4.9
Apr 4, 5:00 PM:

    Fix bug "gotoXY cannot define"
    Improve performance
    You can open file .pas via application
***
**Release**:	3.5.0
Apr 6, 3:21 PM:

    Improve performance or and console.
    TextColor, textBackground working correct;
    You can uses "readln" function with multi variable;
    Supported unicode (use string variable).
***
**Release**:	3.5.3
Apr 10, 12:53 PM: 

    Support full feature with file
    Support decelerate constant with type (const a: integer = 2);
    Fix bug division with integer (1 / 2 = 0 instead 1 / 2 = 0.5)
    Change ui Code Sample
    Fix bug when uses int64 type in for, while, repeat statement
    Support graph (graph is not full support, it is in beta, please see some sample code)
    Add "cardinal" type pascal
    Add Spanish language.
***
**Release**:	3.5.6
Apr 11, 12:25 PM: 

    Fix bug when operate between integer and real numbers
    Supported string length predetermined (var s: string[12])
***
**Release**:	3.5.7
Apr 11, 7:07 PM: 

    Fix bug when operate between integer and real numbers
    Supported string length predetermined (var s: string[12])
    Supported output with format (write(a:3:2))
    Fix bug choose font

***
**Release**:	3.5.9
Apr 15, 11:12 AM: 

    Add library math and strutils (You can help me test library and report bug to me)
    Add feature auto compile (see in setting)
    Support string with size in define type (type str=string[10];)
    Support char range in array (var a: array['a'..'z'] of longint; for c := 'a' to 'z' do ...)

***
**Release**:	3.5.9
Apr 17, 6:43 PM: 

    Supported record type
    Change syntax array as pascal (a[1, 2] instead of a[1][2])
    Add new symbol key

***
**Release**:	3.6.2
Apr 18, 11:55 AM: 

    Fix crash app when select text.
    Support show/hide list symbol below the editor.
    Supported SetFillStyle function
    Complete algorithm floodfill for graph pascal
    Improve reformat code.
    Fix bug keyboard

***
**Release**:	3.6.6
Apr 23, 8:39 AM: 

    Hot: supported debug program
    Support char code: #22 #32
    Fix crash goToLine
    Fix bug comment in code.
    Improve draw text graph and fill object, change triplex font
    Fix bug insert, copy, paste and improve undo redo

***
**Release**:	3.6.7
Apr 24, 2:03 PM: 

    Add some method sysutils lib
    Add textfile type, method assignFile, closeFile
    Improve indent code
    Support dynamic array (setlength procedure)
***
**Release**:	3.6.7
Apr 25, 12:31 PM: 

    Fix bug in library StrUtils
    Add some Android Lib
***
**Release**:	3.6.7
Apr 25, 1:54 PM:

    Fixed bug when run program in prelollipop devices

***
**Release**:	3.6.7
Apr 25, 2:02 PM:

    Fixed bug when run program in prelollipop devices
    Fix bug in library StrUtils
    Add some Android Lib
    Add some method sysutils lib


***
**Release**:	3.6.8
Apr 27, 10:20 PM: 

    Supported decleare arrray constants
    Improve auto suggest code
    Supported speech recognition Android (see sample)
    Support Android Sensor (see sample)
    Fix bug getPixel function in GraphLib
    Fix bug output with multi variable format
    Fix bug clear graph

***
**Release**:	3.6.9
Apr 28, 6:35 PM: 

    Fix bug not found library Android, SysUtils
    Change ui code sample
***
**Release**:	3.7.0
Apr 29, 10:53 PM: 

    Fix bug eof file
    Change single text to fragment page
    More check exception Support compare string
    Fix prolem save file

***
**Release**:	3.7.1
May 1, 2:30 PM: 

    Support pointer type
    Length(array) function.
    SizeOf function
    Erase(file) function
    Low, high function
    Support exit function with parameter
    Find code sample
    Add new language Romanian.
    Add sample.
    Fix bug Fix crash when exit.
    Fix round number
    Supported Speech Recognition Android, Android Sensor

***
**Release**:	3.7.2
May 1, 9:22 PM:

    Fix string index.
***
**Release**:	3.7.3
May 4, 7:42 PM:

    Support scan barcode, qr code... (See sample scan_bar_code.pas in tab Android Sample). This require CAMERA PERMISSION
    Check duplicate variable
    Fix crash app when create file null
    Make CodeSample not launcher
    Support length function with more dimension array
    Ignore case suggest
    Added double type
    Disable some toast

***
**Release**:	3.7.4
Yesterday, 10:33 PM:

    Improve word wrap feature