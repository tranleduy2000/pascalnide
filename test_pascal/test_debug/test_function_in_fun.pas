var
  i, j : integer;

function max(x, y : integer) : integer;
begin
  if x < y then
    max := x
  else
    max := y;
end;

begin
  writeln(max(1, 2), 3);
end.