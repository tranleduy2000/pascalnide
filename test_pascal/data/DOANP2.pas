var
	n: longint;
	a, b: array[1..100000] of longint;

procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do readln(a[i], b[i]);
end;

procedure sort(l, r: longint);
var
	i, j, k1, k2, tmp: longint;
begin	
	i := l;
	j := r;
	k1 := a[(l + r) div 2];
	k2 := b[(l + r) div 2];
	repeat
		while (a[i] < k1) or ((a[i] = k1) and (b[i] < k2)) do inc(i);
		while (a[j] > k1) or ((a[j] = k1) and (b[j] > k2)) do dec(j);
		if i <= j then
		begin
			tmp := a[i];
			a[i] := a[j];
			a[j] := tmp;
			
			tmp := b[i];
			b[i] := b[j];
			b[j] := tmp;
			
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if j > l then sort(l, j);
end;

function min(x: longint): longint;
begin
	if x < 0 then exit(0)
	else exit(x);
end;

procedure process;
var
	i, res, r: longint;
begin
	r := b[1];
	res := b[1] - a[1];
	//writeln('res = ' , res);
	for i := 2 to n do
	begin
		//writeln(r, ' ', b[i], ' ', res);
		if a[i] > r then
		begin
			res := res + (b[i] - a[i]);
			r := b[i];
		end
		else
		begin
			res := min(b[i] - r) + res;
			if r < b[i] then r := b[i];
		end;
	end;
	write(res);
end;

begin
	readf;
	sort(1, n);
	process;
end.
