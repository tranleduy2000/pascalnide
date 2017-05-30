var
	a: array[1..1000] of longint;
	c: array[1..50, 1..50] of boolean;
	
procedure readf;
var f: text;
	i: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do read(f, a[i]);
	close(f);
end;

procedure build;
begin
	for i := 1 to n do c[i, a[i]] := true;
	for i := 1 to n do
		for j := 1 to n do
			if i = a[j] then c[i, a[j]] := true;
end;

begin
	readf;
end.
