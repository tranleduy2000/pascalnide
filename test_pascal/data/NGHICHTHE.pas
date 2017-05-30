var
	a, b, pos, bit: array[1..100000] of longint;
	i, n: longint;
	
procedure readf;
begin
	readln(n);
	for i := 1 to n do
	begin
		read(a[i]);
		pos[i] := i;
	end;
end;

procedure swap(var x, y: longint);
var
	tmp: longint;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

procedure sort(l, r: longint);
var
	k: int64;
	i, j: longint;
begin
	if r <= l then exit;
	i := l; j := r;
	k := a[(l+r) div 2];
	repeat
		while (a[i] < k) do inc(i);
		while (a[j] > k) do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			swap(pos[i], pos[j]);
			inc(i); dec(j);
		end;
	until i > j;
	sort(i, r);
	sort(l, j);
end;

procedure zip;
var
	last, count, i: longint;
begin
	last := 0;
	count := 0;
	for i := 1 to n do
	begin
		if a[i] <> last then
		begin
			last := a[i];
			inc(count);
		end;
		b[pos[i]] := count;
	end;
end;

procedure update(i: longint);
begin
	while i <= n do
	begin
		inc(bit[i]);
		inc(i, i and -i);
	end;
end;

function get(i: longint): longint;
var
	res: longint;
begin
	res := 0;
	while i > 0 do
	begin
		inc(res, bit[i]);
		dec(i, i and -i);
	end;
	exit(res);
end;

procedure process;
var
	i: longint;
	res: int64;
begin
	res := 0;
	for i := n downto 1 do
	begin	
		inc(res, get(b[i]));
		update(b[i] + 1);
	end;
	writeln(res);
end;

begin
	readf;
	sort(1, n);
	zip;
	process;
end.
