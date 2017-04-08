package com.duy.pascal.frontend.code;

/**
 * Created by Duy on 12-Feb-17.
 */
public class CodeSample {
    public static final String MAIN =
            "program abc;\n" +
                    "uses crt; \n" +
                    "begin\n" +
                    "\t" +
                    "\treadln;\n" +
                    "end.";

    public static final int DEFAULT_POSITION = 33;

    public static final String DEMO_THEME = "program array_example;\n" +
            "var\n" +
            "    simpleArray: array[1..3] of integer;\n" +
            "    complexArray: array[1..3, 1..3] of integer;\n" +
            "    arrayOfString: array[-3..-1] of string;\n" +
            "    i, j: integer;\n" +
            "begin\n" +
            "    {set text for simpleArray}\n" +
            "    for i:=2 to 3 do simpleArray[i] := i * i;\n" +
            "    {write to console}\n" +
            "    for i:=1 to 3 do writeln(simpleArray[i]);\n" +
            "    {set text for complexArray}\n" +
            "    for i:=2 to 3 do\n" +
            "        for j:=1 to 2 do complexArray[i][j] := i * j;\n" +
            "    writeln('complex array:');\n" +
            "    {write to console}\n" +
            "    for i:=1 to 3 do for j:=1 to 3 do writeln('[',i,',',j,']=',complexArray[i][j]);\n" +
            "\n" +
            "    arrayOfString[-2] := 'hello pascal';\n" +
            "    writeln(arrayOfString[-3],arrayOfString[-2]);\n" +
            "    readln;\n" +
            "end.";
}
