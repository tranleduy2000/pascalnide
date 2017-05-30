const
	m = 1000000;
var
	n, k: longint;
	f: array[0..1001] of longint;
procedure process;
var
	i: longint;
begin
	readln(n, k);
	f[1] := 2;
	f[0] := 1;
	for i := 2 to n do	
		if i > k then f[i] := f[i-1] mod m + f[i-k-1] mod m
		else f[i] := f[i-1] mod m + 1;
	write(f[n] mod m);
end;

begin
	process;
end.
