var
	n, m, k, res: longint;
	
function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;

begin
	readln(n, m, k);
	res := min(n div 2, m);
	n := n - res * 2;
	m := m - res;
	if k > n + m then
	begin
		k := k - n - m;
		if k mod 3 = 0 then
		dec(res, k div 3)
		else dec(res, k div 3 + 1);
	end;
	writeln(res);
end.
