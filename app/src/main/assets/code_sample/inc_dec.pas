program inc_dec;
var a: integer;
begin
    a := 1;
    inc(a);
    writeln('a + 1 = ', a);
    dec(a);
    writeln('a - 1 = ', a);
    readln;
end.