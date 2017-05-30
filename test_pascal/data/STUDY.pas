var
	a: array[1..100, 1..100] of longint;
	f: array[0..101, 0..101] of longint;
	trace: array[0..100, 0..100] of longint;
	r: array[1..100] of longint;
	n, m: longint;
procedure readf;
var	
	i, j: longint;
begin
	readln(n, m);
	for i := 1 to n do
		for j := 1to m do read(a[i, j]);
end;

function max(x, y: longint): longint;
begin
	if x> y then exit(x) else exit(y);
end;

procedure dp;
var
	i, j, k: longint;
begin
	fillchar(f, sizeof(f), 0);
	for i := 1 to n do
	begin
	    for j := 1 to m do
	    begin
			f[i, j] := f[i-1, j];
			trace[i, j] := 0;
			//sl ngay on mon i khi co j
			for k := 1 to j do
				if f[i, j] < f[i-1, j-k] + a[i, k] then
				begin 
					f[i, j] :=  f[i-1, j-k] + a[i, k];
					trace[i, j] := k;
				end;
		end;
	end;
	writeln(f[n, m]);
	j := m;
	
	for i := n downto 1 do
	begin
		r[i] := trace[i, j];
		j := j - trace[i, j];
	end;
	for i := 1to n do writeln(r[i]);
end;

begin
	readf;
	dp;
end.
