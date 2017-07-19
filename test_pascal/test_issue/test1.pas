const
  n = 5;

type
  Tmas = array [1..n] of integer;

  Student = record
    Marks : Tmas;
  end;

var
  i : integer;
  te : Student;
begin
  for i:= 1 to n do     te.Marks[i] := i + 5;
  for i:= 1 to n do     writeln(te.Marks[i]);
end.