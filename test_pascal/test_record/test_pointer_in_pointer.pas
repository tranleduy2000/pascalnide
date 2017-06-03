type
  TPoint = ^point;

  point = record
    x : ^Integer;
    c : TPoint;
  end;
var
  p : TPoint;
begin
  p^.x^ := 2;
end.