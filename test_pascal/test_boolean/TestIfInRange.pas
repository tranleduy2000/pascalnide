var
    i: longint;
begin
    i := 2;
    if i in [1..1000] then writeln('OK')
    else writeln('FAILED');
end.