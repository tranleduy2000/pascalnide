program case_of_statement;
var
    n: integer;
begin
    writeln('Enter number smaller than 5 (n < 5)');
    readln(n);
    case n of
        1: writeln('one');
        2: writeln('two');
        3: writeln('three');
        4: writeln('four');
    end;
    readln;
end.
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}