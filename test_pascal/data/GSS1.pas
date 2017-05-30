const maxn = 50010 ; oo  = -round(1e9);
type node = record ans, pre, suf, sum: longint; end;
var t: array[1..maxn*4] of node;
	a: array[1..maxn] of longint;
	n, q, qx, qy:longint;

function getNode(sum, pre, suf, ans: longint): node;
var tmp: node;
begin 
	tmp.sum := sum;
	tmp.pre := pre;
	tmp.suf := suf;
	tmp.ans := ans;
	exit(tmp);
end;

function max(x, y: longint): longint;
begin
	if x > y then exit(x) else exit(y);
end;

function add(left, right: node): node;
var tmp: node;
begin
	tmp.sum := left.sum + right.sum;
	tmp.pre := max(left.pre, left.sum + right.pre);
	tmp.suf := max(right.suf, left.suf + right.sum);
	tmp.ans := max(max(left.ans, right.ans), left.suf + right.pre);
	exit(tmp);
end;

procedure buildIT(k, l, r: longint);
var mid: longint;
begin
	if l = r then
	begin
		t[k] := getNode(a[l], a[l], a[l], a[l]);
		exit;
	end;
	mid := (l + r) div 2;
	buildIT(k*2, l, mid);
	buildIT(k*2+1, mid +1, r);
	t[k] := add(t[k*2], t[k*2+1]);
end;

function get(k, l, r, i, j: longint): node;
var mid: longint;
	tmp1, tmp2: node;
begin
	if (j < l) or (r < i) then
	begin
		get.ans := oo;
		exit;
	end;
	if (i <= l) and (r <= j ) then exit(t[k]);
	mid := (l + r) div 2;
	tmp1 := get(k*2, l, mid, i, j);
	tmp2 := get(k*2 + 1, mid +1, r, i, j);
	if tmp1.ans = oo then exit(tmp2);
	if tmp2.ans = oo then exit(tmp1);
	tmp1 := add(tmp1, tmp2);
	exit(tmp1);
end;
	
procedure input;
var f: text;
	i: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do read(f, a[i]);
	buildIT(1, 1, n);
	readln(f, q);
	for i :=1 to q do 
	begin
		read(f, qx, qy);
		writeln(get(1, 1, n, qx, qy).ans);
	end;
	close(f);
end;

begin
	input;
end.
