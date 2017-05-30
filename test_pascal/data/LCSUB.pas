var
	n, k: longint;
	a: array[1..100000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n, k);
	for i := 1 to k do readln(a[i]);
end;

procedure sort(l, r: longint);
var
	k, i, j, tmp: longint;
begin
	i := l;
	j := r;
	k := a[(l + r) div 2];
	repeat
		while a[i] < k do inc(i);
		while a[j] > k do dec(j);
		if i <= j then
		begin
			tmp := a[i];
			a[i] := a[j];
			a[j] := tmp;	
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if j > l then sort(l, j);
end;

procedure dp;
var
	i, j, max: longint;
	f: array[0..100001] of longint;
begin
//	for i :=1 to k do write(a[i], ' ');
	//goi f[i] la do dai day tang lien tiep dai nhat co gia tri cuoi la i
	fillchar(f, sizeof(f), 0);
	for i := 1 to k do inc(f[a[i]]);
	
	//for i := 1 to n do write(f[i], ' ');
	//writeln;
	for i := 2 to n do 
		if (f[i] <> 0) then f[i] := f[i-1] + 1;
	
	//for i := 1 to n do write(f[i] , ' ');
	//writeln;
	max := -1;
	if f[0] >= 1 then
	begin
	//	writeln('fo');
		for i := 1 to k do
		begin
			if a[i] - f[a[i]] - 1 > 0 then
				if f[a[i]] + f[a[i] - f[a[i]] - 1] + 1 > max then 
				begin
					max := f[a[i]] + f[a[i] - f[a[i]] - 1]  + 1;
			//		writeln('adas ', a[i] - f[a[i]], ' ', f[a[i] - f[a[i]] - 1]);
				end;
		end;
	end
	else
	begin
		for i := 1 to n do if f[i] > max then max := f[i];
	end;
	writeln(max);
end;

BEGIN
	readf;
	//sort(1, k);
	dp;
END.
