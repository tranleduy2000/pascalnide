program for_downto_statement;
var
    i, n: integer;
begin
    writeln('Enter small number: ');
    readln(n);
    for i := n downto 1 do writeln(i);
    readln;
end.
