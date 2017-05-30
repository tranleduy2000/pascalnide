var
  a: array[1..1000] of longint;
  b: array[1..1000000] of longint;
  n, max, i, m: longint;
  f: text;
begin
  assign(f, 'file.inp');
  reset(f);
  readln(f, n);
  m := low(longint);
  for i := 1 to n do
  begin
       read(f, a[i]);
       if m < a[i] then m := a[i];
  end;
  close(f);

  fillchar(b, sizeof(b), 0);
  for i := 1 to n do inc(b[a[i]]);
  max := 0;
  for i := 1 to m do
       if b[max] < b[i] then max := i;

  //max: số xuất hiện nhiều nhất
  //b[max] số lần xuất hiện
  writeln(max);
  writeln(b[max]);
  readln;
end.

