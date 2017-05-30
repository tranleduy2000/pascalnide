var
	f: text;
	a: array[1..1000] of longint;
	i,j, tmp, n, m: integer;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, m);
	for i := 1 to n do readln(f, a[i]);
	close(f);
	for i := 1 to n - 1 do
		for j := n downto i do
			if a[j] < a[j+1] then
			begin
				tmp := a[j];
				a[j] := a[j+1];
				a[j+1] := tmp;
			end;
	writeln(a[m]); //result
end.
