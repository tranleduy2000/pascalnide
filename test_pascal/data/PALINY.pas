var
	n: longint;
	s: ansistring;
	max: longint;
	
procedure try(frist, last: longint);
var dd: longint;
begin
	if frist = last then
	begin
		dd := 1; 
		dec(frist);
		inc(last);
	end else dd := 0;
	
	repeat
		if (frist < 1) or (last > n) then break;
		if s[frist] = s[last] then
		begin
			dec(frist);
			inc(last);
			dd := dd + 2;
		end else break;
	until false;
	if max < dd then max := dd;
end;

procedure process;
var i, j: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	readln(f, s);
	max := 0;
	i := n div 2;
	j := n div 2 + 1;
	while (i > max div 2) and (max div 2 <= n - j) do
	begin
		if i > max div 2 then //1..i ko lon hon max, ko can lan
		begin
			try(i, i);
			try(i, i+1);
		end;
		if (max div 2 <= n - j) then
		begin
			try(j, j);
			try(j, j + 1);
		end;
		i := i -1;
		j := j + 1;
	end;
	writeln(max);
end;

procedure test;
var f: text;
	n,i: longint;
begin
	randomize;
	assign(f, 'file.inp');
	rewrite(f);
	n := 50000;
	writeln(f, n);
	for i := 1to n do write(f, chr(random(25)+ 97) );
	close(f);
end;

begin
	//test;
	process;
end.
