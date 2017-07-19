var
    a: array of integer;
    i: integer;
begin
    SetLength(a, 10);
    for i := 0 to 9 do
        a[i] := i;
    for i := 0 to 9 do
        writeln('a[',i,']=',a[i]);
end.
