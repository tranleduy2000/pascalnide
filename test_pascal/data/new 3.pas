var
	a, b: string;
	rmax, rmin: string;
	n, m: longint;
	c: qword;
	
procedure find(i, j: byte; s: string);
begin
	if (i = n) and (j = m) then
	begin
		if rmin > s then rmin := s;
		if rmax < s then rmax := s;
	end;
	if i + 1 <= n then find(i+1, j, s + a[i+1]);
	if j + 1 <= m then find(i, j+1, s + b[j+1]);
end;

begin
	read(c); str(c, a); n := length(a);
	read(c); str(c, b); m := length(b);
	rmin := '9999999999999'; rmax := '';
	find(0, 0, '');
	writeln(rmin);
	writeln(rmax);
end.
