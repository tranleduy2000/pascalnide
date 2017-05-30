const 
	maxn = 100000;
type
	node = record
		maxV, maxS: qword;
	end;
var
	it: array[1..maxn * 4] of node;
	n: longint;
	a: array[1..maxn] of longint;
	
function getnode(v, s: qword): node;
var
	tmp: node;
begin
	tmp.maxV := v;
	tmp.maxS := s;
	exit(tmp);
end;

function max(x, y: qword): qword;
begin if x > y then exit(x) else exit(y) end;

function union(n1, n2: node): node;
var
	tmp: node;
begin
	tmp.maxV := max(n1.maxV, n2.maxV);
	tmp.maxS := max(max(n1.maxS, n2.maxS), n1.maxV + n2.maxV);
	exit(tmp);
end;

procedure build(k, l, r: longint);
var
	mid: longint;
begin
	if l = r then
	begin
		it[k] := getnode(a[l], 0);
		exit;
	end;
	mid := (l + r) div 2;
	build(k*2, l ,mid);
	build(k*2+1, mid+1, r);
	it[k] := union(it[k*2], it[k*2+1]);
end;

procedure update(k, l, r, i, v: longint);
var
	mid: longint;
begin
//	writeln('update ', k, ' ', l, ' ', r, ' ', i);
	if (i < l) or (i > r) then exit;
	if l = r then
	begin
		it[k] := getnode(v, 0);
		exit;
	end;
	mid := (l + r) div 2;
	update(k*2, l, mid, i, v);
	update(k*2+1, mid + 1, r, i, v);
	it[k] := union(it[k*2], it[k*2+1]);
end;

function get(k, l, r,i, j: longint): node;
var
	mid: longint;
begin
	if (j < l) or (i > r) then exit(getnode(0, 0));
	if (i <= l) and (r <= j) then exit(it[k]);
	mid := (l + r) div 2;
	exit(union(get(k*2, l, mid, i, j), get(k*2+1, mid+1, r, i, j)));
end;

procedure process;
var
	f: text;
	c: char;
	i, u, q: longint;
	v: qword;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do read(f, a[i]);
	build(1, 1, n);
	readln(f, q);
	for i := 1 to q do
	begin
		readln(f, c, u, v);
		if c = 'U' then
			update(1, 1, n, u, v)
	    else
			writeln(get(1, 1, n, u, v).maxS);
		
	end;
	close(f);
end;

begin
	process;
end.
