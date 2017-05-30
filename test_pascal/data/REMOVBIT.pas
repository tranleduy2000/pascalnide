var
	s: ansistring;
	i: longint;
begin
	readln(s);
	for i := 1 to length(s) do
		if s[i] = '0' then break;
	delete(s, i, 1);
	writeln(s);
end.
