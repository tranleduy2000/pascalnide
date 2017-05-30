var s: string;
	k, i: integer;
begin
	readln(s);
	readln(k);
	while k > 0 do
	begin
		for i := 1 to length(s) do
			if s[i] > s[i+1] then
			begin
				dec(k);
				delete(s, i, 1);
				break;
			end;
	end;
	writeln(s);
end.
