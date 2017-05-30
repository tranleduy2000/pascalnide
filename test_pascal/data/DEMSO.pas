const
	maxn = 100000;
var
	a, num: array[1..200000] of longint;
	x, n: qword;
	
procedure init;
var
	i, j: longint;
begin
	fillchar(a, sizeof(a), 0);
	a[1] := 0;
	for i := 2 to maxn do
	begin
		if a[i] = 0 then
			for j := 1 to maxn div i + 1 do 
				if a[i*j] = 0 then a[j*i] := i;
	end;
	
	fillchar(num, sizeof(num), 0);
	for i := 2 to maxn do
	begin
		inc(num[a[i]]);
		//writeln('a[', i,'] = ', a[i]);
	end;
end;

procedure process;
begin
	readln(n);
	while n > 0 do
	begin
		readln(x);
		if x > maxn then writeln(0)
		else writeln(num[x]);
		dec(n);
	end;
end;

begin
	init;
	process;
end.
