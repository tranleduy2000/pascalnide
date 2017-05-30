const maxn = 100010; maxq = 200010;
type query = record x, y, k, num: longint; end;
	 
var q: array[1..maxq] of query;
	a, ida, idq: array[1..maxq] of longint;
	t: array[1..maxn*4] of longint;
	n, m: longint;
	
procedure input;
var i: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do 
	begin
		read(f, a[i]);
		ida[i] := i;
	end;
	readln(f, m);
	for i := 1 to m do
	begin
		readln(f, q[i].x, q[i].y, q[i].k);
		idq[i] := i;
	end;
	close(f);
end;

procedure swap(var x, y: longint);
var tmp:  longint;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

function max(x, y: longint): longint;
begin
	if x > y then exit(x) else exit(y);
end;

procedure sorta(l, r: longint);
var k, i, j: longint;
begin
	i := l;
	j := r;
	k := a[ida[(l + r) div 2]];
	repeat
		while a[ida[i]] < k do inc(i);
		while a[ida[j]] > k do dec(j);
		if i <= j then
		begin
			swap(ida[i], ida[j]);
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sorta(i,r );
	if l < j then sorta(l, j);
end;

procedure sortq(l, r: longint);
var k, i, j: longint;
begin
	i := l;
	j := r;
	k := q[idq[(l + r) div 2]].k;
	repeat
		while q[idq[i]].k < k do inc(i);
		while q[idq[j]].k > k do dec(j);
		if i <= j then
		begin
			swap(idq[i], idq[j]);
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sortq(i,r );
	if l < j then sortq(l, j);
end;

procedure buildIT(k, l, r: longint);
var mid: longint;
begin
	if l = r then
	begin
		t[k] := 1;
		exit;
	end;
	mid := ( l+ r) div 2;
	buildIT(k*2, l, mid);
	buildIT(k*2+1, mid+1, r);
	t[k] := t[k*2] + t[k*2+1];
end;

procedure updateIT(k, l, r, i: longint);
var mid: longint;
begin
	if (i < l) or (r < i) then exit; //range
	if l = r then
	begin
		t[k] := 0;
		exit;
	end;
	mid := (l + r) div 2;
	updateIT(k*2, l, mid, i);
	updateIT(k*2 + 1, mid + 1, r, i);
	t[k] := t[k*2] + t[k*2 + 1];
end;

function get(k, l, r, i, j: longint): longint;
var tmp1, tmp2, mid: longint;
begin
	if (j < l) or (r < i) then exit(0);
	if (i <= l) and (r <= j) then exit(t[k]);
	mid := (l + r) div 2;
	tmp1 := get(k*2, l, mid, i, j);
	tmp2 := get(k*2+1, mid + 1, r, i, j);
	tmp1 := tmp1 + tmp2;
	exit(tmp1);
end;

procedure process;
var i, j: longint;
begin
	input;
	sorta(1, n);
	sortq(1, m);
	buildIT(1, 1, n);
	j := 1;
	for i := 1 to m do
	begin
		while (a[ida[j]] <= q[idq[i]].k) and (j <= n) do
		begin
			updateIT(1, 1, n, ida[j]);
			inc(j);
		end;
		q[idq[i]].num := get(1, 1, n, q[idq[i]].x, q[idq[i]].y);
	end;
	for i := 1 to m do writeln(q[i].num);
end;

begin
	process;
end.
