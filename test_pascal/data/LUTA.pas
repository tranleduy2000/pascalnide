var
	n, c: longint;
	a, b: array[1..100000] of longint;
procedure readf;
var
	i: longint;
begin
	readln(n, c);
	for i := 1to n do readln(a[i], b[i]);
end;

procedure sort(l, r: longint);
var
	i, j, tmp, k: longint;
begin
	i := l;
	j := r;
	k := a[(l +r ) div 2];
	repeat
		while a[i] < k do inc(i);
		while a[j] > k  do dec(j);
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
	if i < r then sort(i,r );
	if l < j then sort(l, j);
end;

procedure greedy;
var
	i, count: longint;
begin
	i := 1;
	count := 0;
	while (c >= a[i]) and (i <= n) do
	begin
		inc(count);
		inc(c, b[i]);
		inc(i);
	end;
	write(count);
end;

begin
	readf;
	sort(1, n);
	greedy;
end.
