var
	a: array[1..200000] of int64;
	i: longint;
	n: longint;
	max, tmp: int64;
begin
	readln(n);
	for i := 1to n do
		readln(a[i]);
	max := low(int64);
	tmp := 0;
	for i := 1 to n do
	begin
		tmp := tmp + a[i];
		if tmp > max then max := tmp;
		if tmp < 0 then tmp := 0;
	end;
	write(max);
end.
