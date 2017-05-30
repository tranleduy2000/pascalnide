const
	dx: array[1..4] of shortint = (-1, 0, +1, 0);
	dy: array[1..4] of shortint = (0, +1, 0, -1);
type 
	node = record
		x, y, h: longint;
	end;
var
	n, m, t: longint;
	c: array[1..1000000] of node;
	a: array[0..1001, 0..1001] of boolean;
	h: array[1..100000] of longint;
	
procedure readf;
var
	i, j, count, v: longint;
begin
	readln(n, m);
	count := 0;
	for i := 1to n do
		for j := 1 to m do 
		begin
			inc(count);
			read(v);
			with c[count] do
			begin
				x := i;
				y := j;
				h := v;
			end;
		end;
	readln(t);
	for i := 1 to t do read(h[i]);
end;

procedure qs(l, r: longint);
var
	i, j, tmp, k: longint;
begin
	i := l;
	j := r;
	k := a[(l+r) div 2].h;
	repeat
		while a[i].h > k do inc(i);
		while a[j].h < k do dec(j);
		if i <= j then
		begin
			tmp := a[i];
			a[i] := a[j];
			a[j] := tmp;
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then qs(i, r);
	if l < j then qs(l, j);
end;

function range(x, y: longint): boolean;
begin
	if (x > 0) and (x <= n) and (y > 0) and (y <= m) then exit(true)
	else exit(false);
end;

procedure dfs(u, v: longint);
var
	x, y, i: longint;
begin
	a[u, v] := false;
	for i := 1 to 4 do
	begin
		x := u + dx[i];
		y := v + dy[i];
		if range(x, y) and (a[x, y])  then dfs(x, y);
	end;
end;

procedure process;
var
	i, count, u, v: longint;
begin
	for i := 1 to t do
	begin
		fillchar(a, sizeof(a), true);
		count := 0;
		for u := 1 to n do
		begin
			if c[u].h <= h[i] then break; 
			if (a[c[u].x, c[u].y]) then
			begin
				inc(count);
			end;
		end;
		write(count, ' ');
	end;
end;

begin
	readf;
	sort(1, n * m);
	process;
end.
