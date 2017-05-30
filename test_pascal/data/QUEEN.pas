var
	a: array[-8..8] of boolean; //cheo tay bac - dong nam
	b: array[0..16] of boolean;
	c: array[1..8] of boolean; //cot
	res: array[1..8] of byte; //hang
	x, y: longint;
	found: boolean;

procedure readf;
begin
	readln(x, y);
	fillchar(a, sizeof(a), true);
	fillchar(b, sizeof(b), true);
	fillchar(c, sizeof(c), true);
	found := false;
	a[x-y] := false;
	b[x+y] := false;
	c[y]   := false;
	res[x] := y;
end;

procedure print;
var
	i, j: byte;
begin
	for i := 1 to 8 do 
	begin
		for j := 1 to 8 do if j = res[i] then write('w')
		else write('.');
		writeln;
	end;
	found := true;
end;

procedure find(i: byte);
var
	j: byte;
begin
	if found then exit;
	if i = x then
	begin
		if i = 8 then print
		else find(i+1);
	end
	else
	begin
		for j := 1 to 8 do
		begin
			if a[i-j] and b[i+j] and c[j] then
			begin
				res[i] := j;
				if i = 8 then print
				else
				begin
					a[i-j] := false;
					b[i+j] := false;
					c[j]   := false;
					find(i+1);
					a[i-j] := true;
					b[i+j] := true;
					c[j]   := true;
				end;
			end;
		end;
	end;
end;

begin
	readf;
	find(1);
end.
