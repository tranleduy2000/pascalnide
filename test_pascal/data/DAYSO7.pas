var
	le, ri,a: array[0..200001] of int64;
	n: longint;

procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do read(a[i]);
end;

function max(x, y: int64): int64;
begin
	if x < y then exit(y) else exit(x);
end;

procedure DP;
var
	i: longint;
	m: int64;
begin
	le[3] := a[1] + a[2] + a[3];
	for i := 4 to n do
	begin
		le[i] := a[i] + a[i-1] + a[i-2];
		if le[i] + le[i-3] > le[i] then le[i] := le[i-3] + le[i];
	end;
	
	m := le[3];
	for i := 4 to n do
	begin
		le[i] := max(le[i], m);
		m := le[i];
	end;

	ri[n-2] := a[n] + a[n-1] + a[n-2];
	for i := n - 3 downto 1 do
	begin
		ri[i] := a[i] + a[i+1] + a[i+2];
		if ri[i] + ri[i+3] > ri[i] then ri[i] := ri[i] + ri[i + 3];
	end;
	
	m := low(longint);
	for i := n - 3 downto 1 do
	begin
		ri[i] := max(ri[i], m);
		m := ri[i];
	end;
	
	//for i := 1 to n do write(le[i], ' '); writeln;
	//for i := 1 to n do write(ri[i], ' '); writeln;
	m := low(longint);
	for i := 3 to n-3 do
	begin
		m := max(m, le[i] + ri[i+1]);
	end;
	write(m);
end;

BEGIN
	readf;
	DP;
END.
	
