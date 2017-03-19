program for_statement;
var
    i, n: integer;
begin
    writeln('Enter small number: ');
    readln(n);
    for i := 1 to n do writeln(i);
    writeln;
    for i := n downto 1 do writeln(i);
    readln;
end.
