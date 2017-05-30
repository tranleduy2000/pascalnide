var
	a: array[1..100000] of longint;
	n, k: longint;
	
procedure sort(l, r: longint);
var
	i, j, t, k: longint;
begin
	i := l;
	j := r;
	k := a[(l+r) div 2];
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
	if i < r then sort(i,r );
	if l < j then sort(l, j);
end;

function bs(k, l, r: longint): boolean;
var
	m: longint;
begin
	while l <= r do
	begin
		m := (l + r + 1) div 2;
		if a[m] = k then exit(true);
		if a[m] > k then r := m - 1
		else l := m + 1;
	end;
	exit(false);
end;

procedure process;
var
	i, c: longint;
begin
	c := 0;
	for i := n downto 1 do
	begin
		if a[i] <= k then break;
		if bs(a[i] - k, 1, i-1) then inc(c);
	end;
	write(c);
end;

procedure readf;
var
	i: longint;
begin
	readln(n, k);
	for i := 1 to n do read(a[i]);
end;

begin
	readf;
	sort(1, n);
	process;
end.
