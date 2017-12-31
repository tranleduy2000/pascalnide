type
  TPoint = ^point;

  point = record
    x : ^Integer;
    c : TPoint;
  end;
var
  p : TPoint;
begin
  new(p);
  p^.x^ := 2;
end.