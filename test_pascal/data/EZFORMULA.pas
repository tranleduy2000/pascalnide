var
	a: array[1..10] of 0..1;
	i, j, k, r: longint;
	
begin
	for i := 1to 10 do read(a[i]);
	r := a[1] or a[2];
	for j := 3 to 10 do r := r xor (a[1] or a[j]);
	
	for i := 2 to 9 do
		for j := i + 1 to 10 do r := r xor (a[i] or a[j]);
	
	for i := 1 to 8 do
		for j := i + 1 to 9 do
			for k := j + 1to 10 do r := r xor (a[i] or a[j] or a[k]);
	write(r);
end.
