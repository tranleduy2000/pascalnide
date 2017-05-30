var
	s, res: ansistring;
	err, i, num: longint;
procedure process;
var
	f: text;
	index: longint;
	tmp: ansistring;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, s);
	res := '';
	while s <> 'R0C0' do
	begin
		index := pos('C', s);
		tmp := copy(s, index + 1, length(s) - index);
		val(tmp, num, err);
		while num > 0 do
		begin
			index := (num - 1) mod 26 + 1;
			res := chr(index + 64) + res;
			num := (num - index) div 26;
		end;
		
		writeln(res +copy(s, 2, pos('C',s) - 2));
		res := '';
		readln(f, s);
	end;
	
	close(f);
end;
begin
	process;
end.
