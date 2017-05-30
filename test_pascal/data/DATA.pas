var
	n: longint;
	a: array[0..100001] of longint;
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do read(a[i]);
end;

procedure process;
var
	index, count, i: longint;
begin
	i := 1;
	count := 0;
	while i <= n do
	begin
		while (a[i] < a[i+1]) and (i < n) do inc(i);
		inc(i);
		if i <= n then
		index := i;
		inc(count);
	end;
	if (count = 2) and (a[n] < a[1]) then write(index) 
	else write(-1);
end;

begin
	readf;
	process;
end.
