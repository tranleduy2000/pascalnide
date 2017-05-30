const
	maxn = 300001;
type
	node = record
		a, b, ind: longint;
	end;
	
var
	a: array[0..maxn] of node;
	bit: array[0..maxn] of longint;
	n: longint;
	
procedure sort(l, r: longint);
var
	k, i, j, k2: longint;
	tmp: node;
begin
	if r <= l then exit;
	i := l; j := r; 
	k := a[(l+r) div 2].a; 
	k2 := a[(l+r) div 2].b;
	repeat
		while (a[i].a < k) or ((a[i].a = k) and (a[i].b < k2)) do inc(i);
		while (a[j].a > k) or ((a[j].a = k) and (a[j].b > k2)) do dec(j);
		if i <= j then
		begin
			tmp := a[i]; a[i] := a[j]; a[j] := tmp;
			inc(i); dec(j);
		end;
	until i > j;
	sort(i, r);
	sort(l, j);
end;

//goi bit[i] la so luong so nho hon nut i
procedure update(i,v : longint);
begin 
	while i <= maxn do
	begin
		inc(bit[i], v);
		inc(i, i and -i);
	end;
end;

function get(i: longint): longint;
var  res: longint;
begin
	res := 0;
	while i > 0 do
	begin
		inc(res, bit[i]);
		dec(i, i and -i);
	end;
	exit(res);
end;

procedure process;
var
	f: text;
	i: longint;
	res: array[1..maxn] of longint;
begin
	assign(f, 'file.inp'); reset(f);
	readln(f, n);
	for i := 1 to n do
	begin
		readln(f, a[i].a, a[i].b);
		a[i].ind := i;
	end;
	close(f);
	
	sort(1, n);
	fillchar(bit, sizeof(bit), 0);
	res[a[1].ind] := 0;
	update(a[1].b + 1, 1);
	
	for i := 2 to n do
	begin
		if (a[i].a > a[i-1].a) or (a[i].b > a[i-1].b) then //-> a[i].b + 1 > a[i-1].b
			res[a[i].ind] := get(a[i].b+1)
		else  //a[i].a = a[i-1].a -> a[i].b > a[i-1].b
			res[a[i].ind] := res[a[i-1].ind];
		update(a[i].b+1, 1);
	end;
	for i := 1 to n do writeln(res[i]);
end;

begin
	process;
end.
