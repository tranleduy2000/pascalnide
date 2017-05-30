const 
	maxn = 100000;
var
	it: array[1..maxn * 4] of longint;
	a: array[1..maxn* 4] of boolean;
	n: longint;
	
procedure down(k, l, r: longint);
begin
	if a[k] then
	begin
		a[k] := false;
		a[k*2] := not a[k*2];
		a[k*2+1] := not a[k*2+1];
		it[k] := (r - l + 1) - it[k];
	end;
end;

procedure update(k, l, r, i, j: longint);
var
	mid: longint;
begin
	//writeln('update ', k, ' ', l, ' ', r,' ',i, ' ', j);
	
	down(k, l, r);
	
	if (j <l) or (i > r) then exit;
	if (i <= l) and (r <= j) then
	begin
		it[k] := (r - l + 1) - it[k];
		a[k*2] := not a[k*2];
		a[k*2+1] := not a[k*2+1];
		exit;
	end;
	
	
	mid := (l + r) div 2;
	update(k*2, l, mid, i, j);
	update(k*2+1, mid+1,r, i, j);
	it[k] := it[k*2] + it[k*2+1];
end;

function get(k, l, r, i, j: longint): longint;
var
	t1, t2, mid: longint;
	
begin
	down(k, l, r);
	
	if (j < l) or (i > r) then exit(0);
	if (i <= l) and (r <= j) then exit(it[k]);

	mid := (l + r) div 2;
	t1 := get(k*2, l, mid, i, j);
	t2 := get(k*2 + 1, mid + 1, r, i, j);
	exit(t1 + t2);
end;

procedure process;
var
	f: text;
	m , i, c, u, v: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, m);
	fillchar(it, sizeof(it), 0);
	fillchar(a, sizeof(a), false);
	for i := 1 to m do
	begin
		readln(f, c, u, v);
		if c = 0 then
		begin
			update(1, 1, n, u, v);
		end else
			writeln(get(1, 1, n, u, v));
	end;
	close(f);
end;

begin
	process;
end.
