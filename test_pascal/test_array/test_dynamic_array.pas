program exDynarray;
var
  a : array of array of integer;
    (* a 2 dimensional array *)
  i, j : integer;

begin
  setlength(a, 5, 5);
  for i:=0 to 4 do
    for j:=0 to 4 do
      a[i, j] := i * j;

  for i:=0 to 4 do
  begin
    for j:= 0 to 4 do
      write(a[i, j]: 2, ' ');
    writeln;
  end;
end.