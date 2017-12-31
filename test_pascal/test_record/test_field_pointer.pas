type
  point = record
    x : ^Integer;
  end;
var
  p : point;
begin
  p.x^ := 3;
  writeln(p.x);
  writeln(p.x^);
end.