var s: ansistring;
	f: array[0..2001, 0..2001] of boolean;
	n: longint;
	c, trace: array[0..2001] of longint;
	res: array[1..2000] of ansistring;
function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;
procedure dp;
var i, j, count: longint;
begin
	fillchar(f, sizeof(f), false);
	for i := 1 to n do 
	begin
		f[i, i-1] := true;
		f[i, i] := true;
	end;

	for j := 2 to n do
		for i := 1 to j-1 do
			if (s[i] = s[j]) and (f[i+1, j-1]) then f[i, j] := true;
		
	trace[1] := 0;
	c[0] := 0;
	for i := 1 to n do
	begin
		c[i] := c[i-1] + 1;
		trace[i] := i;
		for j := i - 1 downto 1 do
		begin
			if f[j, i] then 
				if c[i] >= c[j-1] + 1 then
				begin
					c[i] := c[j-1] + 1;
					trace[i] := j;
				end;
		end;
	end;
	writeln(c[n]);
	i := n;
	count := 0;
	while i > 0 do
	begin
		j := trace[i];
		inc(count);
		res[count] := (copy(s, j, i - j + 1));
		i := j-1;
	end;
	for i := count downto 1 do writeln(res[i]);
end;

begin
	readln(n);
	readln(s);
	dp;
end.
