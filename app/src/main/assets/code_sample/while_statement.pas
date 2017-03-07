program while_statement;
var
    i:integer;
    sum: integer;
BEGIN
    i := 1;
    sum := 0;
    While i <= 1000 do
    begin
        sum := sum + i;
        i := i + 1;
    end;
    writeln('Sum of 1 to 1000 is: ');
    write(sum);
    readln;
END.
