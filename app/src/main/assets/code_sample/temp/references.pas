type
    arr = array[1..3] of integer;
var
    a: integer;
    b: array[1..3] of integer;

procedure setv(var i: integer);
begin
    i := 2;
end;

procedure setarr(var c: arr);
begin
    c[1] := 2;
    c[2] := 3;
    c[3] := 6;
end;

begin
    setv(a);
    setarr(b);
    writeln(a);
    writeln(b[1], b[2], b[3]);
end.
