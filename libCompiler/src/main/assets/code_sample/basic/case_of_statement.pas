program case_of_statement;
var
    n: integer;
begin
    repeat
        write('Enter number smaller than 5 (n <= 4) : ');
        readln(n);
        if n > 4 then
        begin
            writeln(n,'>',4);
            writeln('Retry !');
        end;
    until n <= 4;
    case n of
        0: writeln('zero');
        1: writeln('one');
        2: writeln('two');
        3: writeln('three');
        4: writeln('four');
    end;
    readln;
end.
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}
