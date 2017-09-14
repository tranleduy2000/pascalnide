const
  fi = 'detru.inp';
  fo = 'detru.out';
  oo = maxlongint;
type
  contro = ^re;

  re = record
    v, cb, cx : int64;
    next : contro;
  end;

var
  ds1, ds2 : array[0..1000000] of contro;
  n, m, k, nheap : longint;
  d, d1, d2, d3, h, pos : array[0..1000000] of int64;

procedure init1(u, v, cb, cx : longint);
var
  tg : contro;
begin
  new(tg);
  tg^.v := v;
  tg^.cb := cb;
  tg^.cx := cx;
  tg^.next := ds1[u];
  ds1[u] := tg;
end;

procedure init2(u, v, cb, cx : longint);
var
  tg : contro;
begin
  new(tg);
  tg^.v := v;
  tg^.cb := cb;
  tg^.cx := cx;
  tg^.next := ds2[u];
  ds2[u] := tg;
end;

procedure upheap(c : longint);
var
  p : longint;
  tg : int64;
begin
  while c div 2 > 0 do
  begin
    p := c div 2;
    if d[h[p]] <= d[h[c]] then exit;
    tg := pos[h[p]];
    pos[h[p]] := pos[h[c]];
    pos[h[c]] := tg;
    tg := h[p];
    h[p] := h[c];
    h[c] := tg;
    c := p;
  end;
end;

procedure downheap(p : longint);
var
  c : longint;
  tg : int64;
begin
  while p * 2 <= nheap do
  begin
    c := p * 2;
    if (c < nheap) and (d[h[c]] > d[h[c + 1]]) then inc(c);
    if d[h[p]] <= d[h[c]] then exit;
    tg := pos[h[p]];
    pos[h[p]] := pos[h[c]];
    pos[h[c]] := tg;
    tg := h[p];
    h[p] := h[c];
    h[c] := tg;
    p := c;
  end;
end;

procedure push(u : longint);
begin
  if pos[u] = 0 then
  begin
    inc(nheap);
    h[nheap] := u;
    pos[u] := nheap;
  end;
  upheap(pos[u]);
end;

function pop : longint;
begin
  pop := h[1];
  h[1] := h[nheap];
  pos[h[1]] := 1;
  dec(nheap);
  downheap(1);
end;

procedure dij1; //d1 1 -> n chi bang xe
var
  i, j, u, v, cx : longint;
  tg : contro;
begin
  for i:=1 to n do
  begin
    d[i] := oo;
    pos[i] := 0;
  end;
  nheap := 0;
  d[1] := 0;
  push(1);
  repeat
    u := pop;
    tg := ds1[u];
    while tg <> nil do
    begin
      v := tg^.v;
      cx := tg^.cx;
      tg := tg^.next;
      if d[v] > d[u] + cx then
      begin
        d[v] := d[u] + cx;
        push(v);
      end;
    end;
  until nheap = 0;
  d1 := d;
end;


procedure dij2; //i -> k chi bang di bo
var
  i, j, u, v, cx, cb : longint;
  tg : contro;
begin
  for i:=1 to n do
  begin
    d[i] := oo;
    pos[i] := 0;
  end;
  nheap := 0;
  d[k] := 0;
  push(k);
  repeat
    u := pop;
    tg := ds2[u];
    while tg <> nil do
    begin
      v := tg^.v;
      cb := tg^.cb;
      tg := tg^.next;
      if d[v] > d[u] + cb then
      begin
        d[v] := d[u] + cb;
        push(v);
      end;
    end;
  until nheap = 0;
  d2 := d;
end;

procedure dij3; //i -> n chi bang xe
var
  i, j, u, v, cx : longint;
  tg : contro;
begin
  for i:=1 to n do
  begin
    d[i] := oo;
    pos[i] := 0;
  end;
  nheap := 0;
  d[n] := 0;
  push(n);
  repeat
    u := pop;
    tg := ds2[u];
    while tg <> nil do
    begin
      v := tg^.v;
      cx := tg^.cx;
      tg := tg^.next;
      if d[v] > d[u] + cx then
      begin
        d[v] := d[u] + cx;
        push(v);
      end;
    end;
  until nheap = 0;
  d3 := d;
end;


procedure doc;
var
  i, u, v, cb, cx : longint;
  res, tmp : int64;
begin
  //  assign(input,fi); reset(input);
  //  assign(output,fo); rewrite(output);
  read(n, m, k);
  for i:=1 to m do
  begin
    read(u, v, cb, cx);
    init1(u, v, cb, cx);
    init2(v, u, cb, cx);
  end;
  dij1;
  dij2;
  dij3;
  res := oo;
  tmp := oo;
  for i:=1 to n do
    if (d1[i] + d2[i] <= 59) and (d1[i] + d3[i] < tmp) then
    begin
      res := d1[i] + d2[i] - d2[k];
      tmp := d1[i] + d3[i];
    end;
  write(tmp);
end;

begin
  doc;
end.
    
