function fib(n : Integer) : Integer;
var
  f : Integer;
begin
  if n < 1 then fib := fib(n + 2) - fib(n + 1)
  else if n > 2 then fib := fib(n - 2) + fib(n - 1)
  else fib := 1;
end;

begin
  Write(fib(10));
end.
