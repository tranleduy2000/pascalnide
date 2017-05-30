var
	x , y: array[1..4] of longint;
	r : longint;
	
procedure find;
var
	i, j, k: longint;
begin
	for i := 1 to 2 do
		for j := i + 1 to 3 do
			if x[i] = x[j] then
			begin
				for k := 1 to 4 do
					if (i <> k) and (j <> k) then
					begin
						 r := k;
						 break;
					end;
				exit;
			end;
end;

procedure process;
var
	 j, k: longint;
begin
	find;
//	writeln(r);
	x[4] := x[r];
	for j := 1 to 3 do
		if (y[r] = y[j]) and (j <> r) then
		begin
			write('r = ',r);	writeln('j = ', j);
			for k := 1 to 3 do
				if (k <> j) and (k <> r) then
				begin
			//		writelnsv(k);
					y[4] := y[k];
					break;
				end;
		end;
	writeLn(x[4], ' ',y[4]);
end;
begin
	readln(x[1], y[1], x[2], y[2], x[3], y[3]);
	process;
end.
