package com.duy.pascal.frontend.code;

/**
 * Created by Duy on 12-Feb-17.
 */
public class CodeSample {
    public static final String MAIN =
            "program abc;\n" +
                    "//uses crt; \n" +
                    "begin\n" +
                    "   \n" +
                    "end.";

    public static final int DEFAULT_POSITION = 33;

    public static final String DEMO_THEME = "program array_example;\n" +
            "var\n" +
            "\tsimpleArray: array[1..3] of integer;\n" +
            "\tcomplexArray: array[1..3, 1..3] of integer;\n" +
            "\tarrayOfString: array[-3..-1] of string;\n" +
            "\ti, j: integer;\n" +
            "begin\n" +
            "\t//set text for simpleArray\n" +
            "\tfor i:=2 to 3 do simpleArray[i]:=i*i;\n" +
            "\t//write to console\n" +
            "\tfor i:=1 to 3 do writeln(simpleArray[i]);\n" +
            "\t//set text for complexArray\n" +
            "\tfor i:=2 to 3 do\n" +
            "\t\tfor j:=1 to 2 do complexArray[i][j]:=i*j;\n" +
            "\twriteln('complex array:');\n" +
            "\t//write to console\n" +
            "\tfor i:=1 to 3 do for j:=1 to 3 do writeln('[',i,',',j,']=',complexArray[i][j]);\n" +
            "\n" +
            "\tarrayOfString[-2]:='hello pascal';\n" +
            "\twriteln(arrayOfString[-3],arrayOfString[-2]);\n" +
            "\t//readln;\n" +
            "end.";
}
