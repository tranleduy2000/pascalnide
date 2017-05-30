var
	a: array[1..100000] of int64;
	n, i, j, k: longint;
	max: int64;
begin
	readln(n, k);
	for i := 1 to n do
	begin
		readln(a[i]);
		a[i] := (a[i-1] + a[i]) mod k;
	end;
	max := 0;
	for i := 1 to n do
	begin
		for j := i to n do
		begin
			if ((a[j] - a[i-1]) mod k = 0) then
				if (j - i + 1 > max) then max := j - i + 1;
		end;
		if n - i + 1 < max then break;
	end;
	write(max);
end.
