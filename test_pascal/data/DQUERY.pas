var
	it: array[1..maxn] of byte;
	n: longint;
	
procedure buildit(k, l, r: longint);
begin
end;
	
procedure process;
var
	f: text;
	i, u, v, q: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i  := 1to n do read(f, a[i]);
	buildIT(1, 1, n);
	readln(F, q);
	for i := 1 to q do
	begin
		readln(f, u, v);
		writeln(get(1, 1, n, u, v));
	end;
end;

begin
end.
