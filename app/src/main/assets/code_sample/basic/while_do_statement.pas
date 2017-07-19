program while_do_statement;
var
    i: integer;
    sum: integer;
BEGIN
    i := 1;
    sum := 0;
    While i <= 1000 do
    begin
        sum := sum + i;
        i := i + 1;
    end;
    write('Sum of 1 to 1000 is: ');
    write(sum);
    readln;
END.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}