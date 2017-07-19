var
  i : Integer;
  j : longint;
begin
  for i := 1 to 1000000 do j := j + 1;
  WriteLn(j);

end.