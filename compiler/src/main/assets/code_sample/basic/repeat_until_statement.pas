program repeat_until_statement;
var
    i: integer;
    sum: integer;
BEGIN
    i := 1;
    sum := 0;
    writeln('Sum of 1 to 100 is: ');
    repeat
        sum := sum + i;
        i := i + 1;
    until i = 100;
    writeln(sum);
    readln;
END.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}