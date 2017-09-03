type
  num = 1..200;
var
  a : array[0..2, 0..1] of num;
  b : integer;
begin
  a[0, 0] := 1;
  b := a[0, 0] * 10;
  writeln(b);
end.