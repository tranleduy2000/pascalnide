const
  maxn = 100000;
var
  sum : array[1..maxn] of int64;
  a, num : array[0..maxn] of longint;
  n, u, v : longint;
  pos : array[1..maxn] of longint;

procedure swap(var x, y : longint);
var
  tmp : longint;
begin
  tmp := x;
  x := y;
  y := tmp;
end;

procedure sort(l, r : longint);
var
  k : int64;
  i, j : longint;
begin
  if r <= l then exit;
  i := l;
  j := r;
  k := a[(l + r) div 2];
  repeat
    while (a[i] < k) do inc(i);
    while (a[j] > k) do dec(j);
    if i <= j then
    begin
      swap(a[i], a[j]);
      swap(pos[i], pos[j]);
      inc(i);
      dec(j);
    end;
  until i > j;
  sort(i, r);
  sort(l, j);
end;


procedure readf;
var
  i, last, count : longint;
begin
  readln(n);
  for i := 1 to n do
  begin
    read(a[i]);
    pos[i] := i;
  end;
  sort(1, n);
end;

procedure update(i : longint; v : int64);
begin
  while i <= n do
  begin
    inc(num[i]);
    inc(sum[i], v);
    inc(i, i and -i);
  end;
end;


procedure get(i : longint);
begin
  u := 0;
  v := 0;
  while i > 0 do
  begin
    inc(u, num[i]);
    inc(v, sum[i]);
    dec(i, i and -i);
  end;
end;


procedure process;
var
  res : qword;
  i : longint;
begin
  update(pos[1], a[1]);
  res := 0;
  for i := 2 to n do
  begin
    get(i);

    writeln(a[i], ' ', u, ' ', v, ' ', v - (i - 1 - u) * a[i]);
    inc(res, v - (i - 1 - u) * a[i]);

    update(pos[i], a [i]);
  end;
//	writeln(res);
end;

begin
  readf;
  process;
end.
