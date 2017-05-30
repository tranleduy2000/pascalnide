var
	n: longint;
	f: array[0..1000000, 0..20] of longint;

function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;

procedure init;
var
	i, j: longint;
begin
	readln(n);
	for i := 1 to n do read(f[i, 0]);
	for j := 1 to 20 do
		for i := 1 to n - (1 shl j) + 1 do
			f[i, j] := min(f[i, j-1], f[i + (1 shl (j-1)), j-1]);
		
end;

function get(l, r: longint): longint;
var
	len, k: longint;
begin
	len := r - l + 1;
	k := trunc(ln(len) / ln(2));
	exit(min(f[l, k], f[r - (1 shl k) + 1, k]));
end;

procedure process;
var
	q, l, r, i: longint;
begin
	readln(q);
	for i := 1 to q do
	begin
		readln(l, r);
		writeln(get(l+1, r+1));
	end;
end;
	
begin
	init;
	process;
end.
