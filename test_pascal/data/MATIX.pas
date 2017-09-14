const
  maxn = 1024 + 10;
var
  bit : array[0..maxn, 0..maxn] of longint;
  n : integer;

procedure update(x, y, v : integer);
var
  tmp : integer;
begin
  //  writeln('update ', x,' ', y , ' ' ,v);
  while x <= n do
  begin
    tmp := y;
    while tmp <= n do
    begin
      inc(bit[x, tmp], v);
      inc(tmp, tmp and -tmp);
    end;
    inc(x, x and -x);
  end;
end;

function get(x, y : integer) : integer;
var
  res, tmp : integer;
begin
  //      write('get ', x, ' ', y, ' ');
  res := 0;
  while x > 0 do
  begin
    tmp := y;
    while tmp > 0 do
    begin
      inc(res, bit[x, tmp]);
      dec(tmp, tmp and -tmp);
    end;
    dec(x, x and -x);
  end;
  //    writeln('get = ',res);
  exit(res);
end;

procedure process;
var
  f : text;
  test, x, y, v, u : integer;
  c : char;
begin
  assign(f, 'file.inp');
  reset(f);
  readln(f, test);
  while test > 0 do
  begin
    readln(f, n);
    fillchar(bit, sizeof(bit), 0);
    while true do
    begin
      read(f, c);
      read(f, c);
      if c = 'E' then
      begin
        read(f, c);
        readln(f, x, y, v);
        update(x + 1, y + 1, 0);
        update(x + 1, y + 1, v);
      end else if c = 'U' then
      begin
        read(f, c);
        readln(f, x, y, u, v);
        inc(x);
        inc(y);
        inc(u);
        inc(v);
//                                writeln('>> ', x, y, u, v);
        writeln(get(u, v)
        - get(u, y - 1) - get(x - 1, v)
        + get(x - 1, y - 1));
      end else
      begin
        readln(f, c);
        break;
      end;
    end;
    dec(test);
  end;
  close(f);
end;

begin
  process;
  readln;
end.
