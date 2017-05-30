const maxn = 1000;
var a, b, c, d :array[1..maxn] of longint;
	i, n, t, p, cnt: longint;
	ff: boolean;
begin
	cnt := 0;
	readln(n, t, p);
	for i := 1to n do readln(a[i], b[i], c[i], d[i]);
	ff := true;
	while ff do
	begin
		ff := false;
		for i := 1 to n do 
			if (a[i] <> -1) and (t >= a[i]) and (p >= b[i]) then
			begin
				a[i] := -1;
				t := t + c[i];
				p := p + d[i];
				ff := true;
				inc(cnt);
			end;
	end;
	writeln(cnt);
end.
