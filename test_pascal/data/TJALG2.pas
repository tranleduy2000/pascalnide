program TJALG;
const
  maxn = 10011;
var
  a: array[1..maxn, 1..maxn] of boolean;
  low, num, stack: array[1..maxn] of longint;
  free: array[1..maxn] of boolean;
  n, count, top, num_connect: longint;
 
procedure readf;
var
  f: text;
  i, u, v, m: longint;
begin
  assign(f, '');
  reset(f);
  readln(f, n, m);
  fillchar(a, sizeof(a), false);
  for i := 1 to  m do
  begin
    readln(f, u, v);
    a[u, v] := true;
  end;
  close(f);
end;
 
procedure push(u: longint);
begin
  inc(top);
  stack[top] := u;
end;
 
function pop: longint;
begin
  pop := stack[top];
  dec(top);
end;
 
function min(x, y: longint): longint;
begin
  if x < y then min := x else min := y;
end;
 
procedure DFS(u: longint);
var
  v: longint;
begin
  inc(count);
  num[u] := count;
  low[u] := count;
  push(u);
  for v := 1 to n do
      if a[u, v] and free[v] then
      begin
        if num[v] <> 0 then
           low[u] := min(low[u], num[v])
        else
          begin
            DFS(v);
            low[u] := min(low[u], low[v]);
          end;
      end;
  if low[u] = num[u] then
  begin
    inc(num_connect);
    repeat
      free[u] := false;
      v := pop;
    until v = u;
  end;
end;
 
procedure tarjan;
var
  u: longint;
begin
  num_connect:=0;
  fillchar(num, sizeof(num), 0);
  fillchar(free, sizeof(free), true);
  count := 0;
  top := 0;
  for u := 1 to n do
      if num[u] = 0 then DFS(u);
  write(num_connect);
  readln;
end;
begin
  readf;
  tarjan;
end.
 
