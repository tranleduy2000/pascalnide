var
	s: array[0..120, 0..20] of int64;
	i, j, n, k,z: longint; 
	res, sum: int64;
	
function max(x, y: int64): longint;
begin if x > y then exit(x) else exit(y); end;

begin
	readln(n);
	res := -1;
	for i := 1 to n do
		for j := 1 to 11 do read(s[i, j]);
		
	for i := 1 to n - 2 do 
		for j := i + 1 to n - 1 do
			for k := j + 1 to n do
			begin
				sum := 0;
				for z := 1 to 11 do
					sum := sum + max(s[i, z], max(s[j, z], s[k, z]));
				res := max(res, sum);
			end;
	writeln(res);
end.
