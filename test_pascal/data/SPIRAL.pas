const
  maxn = 1000;
  dx : array[1..4] of integer = (-1, 0, 1, 0);
  dy : array[1..4] of integer = (0, 1, 0, -1);
var
  c, a : array[1..1000, 1..1000] of longint;
  free : array[1..1000, 1..maxn] of boolean;
  n, xst, yst, xend, yend, valS, valE : longint;

procedure build;
var
  i, j, num : longint;
begin
  num := 0;
  i := 1;
  j := 0;
  fillchar(a, sizeof(a), 0);
  while num <= n * n - 1 do
  begin
    while (j < n) and (a[i, j + 1] = 0) do
    begin
      inc(j);
      inc(num);
      a[i, j] := num;
    end;
    while (i < n) and (a[i + 1, j] = 0) do
    begin
      inc(i);
      inc(num);
      a[i, j] := num;
    end;
    while (j > 1) and (a[i, j - 1] = 0)do
    begin
      dec(j);
      inc(num);
      a[i, j] := num;

    end;
    while (i > 1) and (a[i - 1, j] = 0) do
    begin
      dec(i);
      inc(num);
      a[i, j] := num;
    end;
  end;
end;

function gcd(x, y : longint) : longint;
begin
  if x = 0 then exit(y);
  if x > y then exit(gcd(x mod y, y))
  else exit(gcd(y mod x, x));
end;

function range(x, y : longint) : boolean;
begin
  if (x < 1) or (x > n) or (y < 1) or (y > n) then exit(false);
  if not free[x, y] then exit(false);
  exit(true);
end;

procedure bfs;
var
  qx, qy : array[1..maxn * maxn] of longint;
  x, y, i, j, u, v, f, r : longint;
begin
  for i := 1 to n do
    for j := 1 to n do
    begin
      if a[i, j] = valS then
      begin
        xst := i;
        yst := j;
      end;
      if a[i, j] = valE then
      begin
        xEnd := i;
        yend := j;
      end;
    end;

  f := 1;
  r := 1;
  qx[1] := xst;
  qy[1] := yst;
//	writeln(xst,  ' ', yst, ' ', xend, ' ', yend);
  fillchar(c, sizeof(c), 0);
  fillchar(free, sizeof(free), true);
  repeat
    x := qx[f];
    y := qy[f];
    inc(f);
    for i := 1 to 4 do
    begin
      u := dx[i] + x;
      v := dy[i] + y;
      if range(u, v) then
      begin
        if gcd(a[u, v], a[x, y]) = 1 then
        begin
          free[u, v] := false;
          c[u, v] := c[x, y] + 1;
          if (u = xEnd) and (v = yEnd) then break;
          inc(r);
          qx[r] := u;
          qy[r] := v;
        end;
      end;
    end;
  until f > r;
  if c[xend, yend] = 0then writeln(-1) else
    writeln(c[xend, yend]);
end;

var
  test : integer;
begin
  readln(test);
  while test > 0 do
  begin
    readln(n, valS, valE);
    if valS = valE then writeln(0)
    else begin
      build;
      bfs;
    end;
    dec(test);
  end;
end.
