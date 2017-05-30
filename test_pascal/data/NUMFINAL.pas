var
	a, n, r, i: longint;
begin
	readln(a, n);
	r := a;
	for i := 2 to n do r := (r * a) mod 10;
	write(r);
	readln;
end.
