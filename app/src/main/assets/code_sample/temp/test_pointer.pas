procedure proc(a: ^integer);
begin
    a^ := 2;
end;

var
    x: integer;
begin
    proc(@x);
    writeln(x)
end.