const
  e = 1000007;
var
  f1, f2, f3, n, i: longint;
begin
  readln(n);
  f1 := 1;
  f2 := 1;
  f3 := 1;
  for i := 3 to n do
      begin
        f3 := (f1 + f2) mod e;
        f1 := f2;
        f2 := f3;
      end;
  write(f3);
  readln;
end.

