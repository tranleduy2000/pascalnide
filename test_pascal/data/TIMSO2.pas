var
	x, t, i, j, tmp: longint;
	res: array[1..10000] of byte;
begin
	readln(x);
	t := 0;
	if x = 0 then 
	begin
		write(10);
		exit;
	end;
	if x < 10 then 
	begin
		write(x);
		exit;
	end;
	for i := 9 downto 2 do
	begin
		while x mod i = 0 do
		begin
			inc(t);
			res[t] := i;
			x := x div i;
			if x = 1 then break;
		end;
		if x = 1 then break;
	end;
	
	if (x < 10)  then
	begin
		if (x > 1) then
		begin
			inc(t);
			res[t] := x;
		end;
		for i := t - 1 downto 1 do
			for j := 1 to i do
				if res[j] > res[j+1] then
				begin
					tmp :=res[j];
					res[j] := res[j+1];
					res[j+1] := tmp;
				end;
		for i := 1 to t do write(res[i]);
	end 
	else write(-1);
end.

