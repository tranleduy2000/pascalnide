uses crt;
var
    c: Char;
begin
    repeat
        c := readkey;
        writeln(c);
    until c = 'q';
end.
