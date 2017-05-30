var n, count: longint;
	x: array[0..20] of byte;
	//'(' 0; ')' 1
procedure check;
var i, cnt: longint;
begin
	cnt := 0;
	for i := 1 to n do
	begin
		if (cnt = 0) and (x[i] = 1) then exit;
		if x[i] = 0 then inc(cnt) else dec(cnt);
	end;
	if cnt = 0 then
	begin
		inc(count);
		for i := 1 to n do 
			if x[i] = 0 then write('(') else write(')');
		writeln;
	end;
end;

procedure next(i: byte);
var j: byte;
begin
	for j := 0 to 1 do
	begin
		x[i] := j;
		if i = n then check else next(i+1);
	end;
end;

begin
	readln(n);
	count := 0;
	next(1);
	writeln(count);
end.
