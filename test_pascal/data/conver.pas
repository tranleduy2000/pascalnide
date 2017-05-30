var fi, fo: text;
	index: longint;
	s: string;
	v, vn, vt: array[1..1000000] of string;
	cv, cvn, cvt: longint;

function removeSpace(s: string): string;
var tmp: string;
begin
	tmp := s;
	while tmp[1] = ' ' do
	begin
		delete(tmp, 1, 1);
	//	writeln('t1 ');
	end;
	while tmp[length(tmp)] = ' ' do
	begin
		delete(tmp, length(tmp), 1);
	//	writeln('t2 =====================');
	end;
	while pos('  ', tmp) > 0 do
	begin
		delete(tmp, pos('  ', tmp), 1);
	//	writeln('t3 =====================');
	end;
	removeSpace := tmp;
end;

procedure processV(s: string);
begin
	delete(s, 1, 1); //fris
	s := removeSpace(s);
	index := pos(' ', s);
	while index > 0 do
	begin
		inc(cv);
		v[cv]:= copy(s, 1, index - 1);
		delete(s, 1, index);
		s := removeSpace(s);
		index := pos(' ', s);
	end;
	if length(S) > 0 then
	begin
		inc(cv);
		v[cv] := s;
	end;	
end;

procedure processVN(s: string);
begin
	delete(s, 1, 2); //fris
	s := removeSpace(s);
	index := pos(' ', s);
	while index > 0 do
	begin
		inc(cvn);
		vn[cvn]:= copy(s, 1, index - 1);
		delete(s, 1, index);
		s := removeSpace(s);
		index := pos(' ', s);
	end;
	if length(s) > 0 then
	begin
		inc(cvn);
		vn[cvn] := s;
	end;
end;

procedure processVT(s: string);
begin
//	writeln(s);
	delete(s, 1, 2); //fris
	//writeln(s);
	
	s := removeSpace(s);
	
	index := pos(' ', s);
	
	//writeln(s, ' ', index);
	
	
	while index > 0 do
	begin
		inc(cvt);
		vt[cvt]:= copy(s, 1, index - 1);
		
	//	writeln(vt[cvt]);
		delete(s, 1, index);
		s := removeSpace(s);
		index := pos(' ', s);
	end;
	if length(s) > 0 then
	begin
		inc(cvt);
		vt[cvt] := s;
	end;
end;

procedure print;
var i: longint;
begin
	assign(fo, 'file.txt');
	rewrite(fo);
	writeln(fo, cv);
	for i := 1 to cv do writeln(fo, v[i]);
	
	writeln(fo, cvn);
	for i := 1 to cvn do writeln(fo, vn[i]);
	
	writeln(fo, cvt);
	for i := 1 to cvt do writeln(fo, vt[i]);
	close(fo);
end;

begin
	assign(fi, 'file.inp');
	reset(fi);
	cv := 0; cvt := 0; cvn := 0;
	while not eof(fi) do
	begin
		readln(fi, s);
		s := removeSpace(s);
		if s[1] = '#' then writeln(s);
		if (s[1] = 'v') and (s[2] = ' ') then processV(s);
		if (s[1] = 'v') and (s[2] = 'n') then processVN(s);
		if (s[1] = 'v') and (s[2] = 't') then processVT(s);
	end;
	close(fi);
	print;
	write(cv, ' ', cvn, ' ',cvt);
end.
