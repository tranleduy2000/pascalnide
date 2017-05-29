program exString;
uses sysutils;
var
    str1, str2, str3 : ansistring;
    str4: string;
    len: integer;

begin
    str1 := 'Hello ';
    str2 := 'There!';

    (* copy str1 into str3 *)
    str3 := str1;
    writeln('appendstr( str3, str1) :  ', str3 );

    (* concatenates str1 and str2 *)
    appendstr( str1, str2);
    writeln( 'appendstr( str1, str2) ' , str1 );
    str4 := str1 + str2;
    writeln('Now str4 is: ', str4);

    (* total lenghth of str4 after concatenation  *)
    len := byte(str4[0]);
    writeln('Length of the final string str4: ', len);
end.