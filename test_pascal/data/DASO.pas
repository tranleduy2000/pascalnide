var
	n: longint;
	a: array[-1..30000] of longint;
	i, j, max: longint;
	
begin
	readln(n);
	for i := 1 to n do read(a[i]);
	max := 0;
	i := 3;
	a[-1] := 0;
	a[0] := a[1];
	while i <= n do
	begin
		j := i;
		while (a[j] = a[j-1] + a[j-2]) and (j <= n) do inc(j);
		if j - i + 2 > max then max := j - i + 2;
		if n - i + 1 < max then break;
		i := j + 1;
	end;
	if max <=2 then write(-1)
	else
	write(max);
end.
