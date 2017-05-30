var
	n, min, max: longint;
	a: array[1..1000] of longint;
	d: array[-33333333..33333333] of boolean;

procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1to n do read(a[i]);
end;

procedure sort(l, r: longint);
var
	i, j, t, k: longint;
begin
	i := l;
	j := r;
	k := a[(l + r) div 2];
	repeat
		while a[i] < k do inc(i);
		while a[j] > k do dec(j);
		if i <= j then
		begin
			t := a[i];
			a[i] := a[j];
			a[j] := t;
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if j > l then sort(l, j);
end;

function bs(k, l, r: longint): longint;
var
	m, tmp: longint;
begin
	while l <= r do
	begin
		m := (l + r + 1) div 2;
		if a[m] = k then
		begin
			r := 1;
			tmp := m;
			while (a[tmp - 1] = k) and (tmp > 1 )do dec(tmp);
			r := r + m - tmp;
			tmp := m;
			while (a[tmp + 1] = k) and (tmp < n) do inc(tmp);
			r := r + tmp - m;
			exit(r);
		end;
		if a[m] > k then r := m - 1
		else l := m + 1;
	end;
	exit(0);
end;

procedure process;
var
	i, j, k, c: longint;
begin
	c := 0;
	fillchar(d, sizeof(d), false);
	readf;
	sort(1, n);
	for i := 1 to n-2 do	
		for j := i + 1  to n-1 do
			for k := j + 1 to n do
				if round((a[i] + a[j] + a[k]) / 3)= (a[i] + a[j] + a[k])/3 then
				begin
					d[(a[i] + a[j] + a[k]) div 3] := true;
					if (a[i] + a[j] + a[k]) div 3 < min then min := (a[i] + a[j] + a[k]) div 3;
					if (a[i] + a[j] + a[k]) div 3 > max then max := (a[i] + a[j] + a[k]) div 3;
				end;
	c := 0;
	for i := min to max do
		if d[i] then c := c + bs(i, 1, n);
	write(c);
	readln;
end;

begin
	process;
end.
