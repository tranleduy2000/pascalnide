var
	n: longint;
	a, f, trace, res: array[0..1000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1to n do read(a[i]);
end;

procedure dp;
var
	max, i, j: longint;
begin
	for i := 1 to n do
	begin
		f[i] := 1;
		trace[i] := 0;
		for j := 1 to i do
		begin
			if (a[j]  < a[i]) and (f[j] + 1 > f[i]) then
			begin
				f[i] :=f[j] + 1;
				trace[i] := j;
			end;
		end;
	end;
	max := f[1];
	j := 1;
	for i := 2 to n do 
	begin
		if f[i] > max then 
		begin
			j := i;
			max := f[i];
		end;
	end;
	writeln(max);
	i := 0;
	//trace;
	while j <> 0 do
	begin
		inc(i);
		res[i] := a[j];
		j := trace[j];
	end;
	for j := i downto 1 do write(res[j], ' ');
end;

begin
	readf;
	dp;
end.
