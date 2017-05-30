const maxC = 100000;
var
	a: array[1..50, 1..50] of longint;
	n, i, j, k: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
		
	for i := 1 to n do 
		for j := 1 to n do 
		begin
			read(f, a[i, j]);
			if a[i, j] = 0 then a[i, j] := maxC;
			if i = j then a[i, j] := 0;
		end;
	close(f);
	
	for i := 1 to n do
		for j := 1 to n do
			for k := 1 to n do
				if a[i, k] + a[k, j] < a[i, j] then
				begin
					a[i, j] := a[i, k] + a[k, j];
				end;
				
	for i := 1 to n do
	begin
		for j := 1 to n do write(a[i, j], ' ');
		writeln;
	end;
end.
