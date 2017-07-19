type
  point = record
    x, y : LongInt;
  end;
var
  p : point;
begin
  with p do
  begin
    y := 3;
    x := 2;
  end;
  WriteLn(p.x, ' ', p.y);
  p.x := 2;
  p.y := 3;
  WriteLn(p.x, ' ', p.y);
end.