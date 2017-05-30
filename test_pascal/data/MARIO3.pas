var
	a: array[-3..1000] of longint;
	f: array[-3..1000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do read(a[i]);
end;

procedure DP;
begin
	f[-2] := 0; f[-1] := 0; f[0] := 1;
	for i := 1 to n do
	begin
		if a[i] = 0 then f[i] := f[i-1] + f[i-2] + f[i-3]
		else if a[i] = 1 then f[i] := f[i-1];
	end;
end;

begin
end.
