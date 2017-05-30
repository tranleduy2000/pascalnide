

function getmax(s: ansistring): longint;
var
	i, j, n, top, max: longint;
	a, f: array[1..1001] of longint;
begin
	top := 0;
	i := 1;
	s := ' ' + s + ' ';
	n:= length(s);
	while i < n do
	begin
		if (s[i] = ' ') and (s[i+1] <> ' ') then
		begin
			j := i + 1;
			while (j <= n) and (s[j] <> ' ') do	inc(j);
			inc(top);
			a[top] := j - i- 1;
			i := j;
		end
		else inc(i);
	end;
	f[1] := 1;
	max := 1;
	for i := 2 to top do
	begin
		if a[i] = a[i-1] then f[i] := f[i-1] + 1
		else f[i] := 1;
		if max < f[i] then max := f[i];
	end;
	exit(max);
end;
	
procedure process;
var
	n, i: longint;
	s: ansistring;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do
	begin
		readln(f, s);
		writeln(getmax(s));
	end;
	close(f);
end;


begin
	process;
end.
