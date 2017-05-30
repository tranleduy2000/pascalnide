const maxn = 50000; oo = -round(1e9);
type node = record sum, pre, suf, ans: longint; end;
var t: array[1..maxn*4] of node;
	n,q: longint;
	a: array[1..maxn] of longint;
	
function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;
function max(x, y: longint): longint;
begin
	if x > y then exit(x) else exit(y);
end;




function add(node1, node2: node): node;
var res: node;
begin
	res.sum := node1.sum + node2.sum;
	res.pre := max(node1.pre, node1.sum + node2.pre);
	res.suf := max(node2.suf, node1.suf + node2.sum);
	res.ans := max(max(node1.ans, node2.ans), node1.suf + node2.pre);
	exit(res);
end;


function getNode(sum, pre, suf, ans: longint): node;
var tmp: node;
begin
	tmp.sum := sum;
	tmp.pre := pre;
	tmp.suf := suf;
	tmp.ans := ans;
	exit(tmp);
end;

procedure buildIT(k, l, r: longint);
var mid: longint;
begin
	if l = r then
	begin
		t[k] := getNode(a[l], a[l] , a[l], a[l]);
		exit;
	end;
	mid :=(l + r) div 2;
	buildIT(k*2, l, mid);
	buildIT(k*2+1, mid + 1, r);
	t[k] := add(t[k*2], t[k*2+1]);
end;

function get(k, l, r, i, j: longint): node;
var mid: longint;
	tmp1, tmp2: node;
begin
	if (j < l) or (r < i) then 
	begin
		tmp1 := getNode(0, 0, 0, oo);
		exit(tmp1);
	end;
	if (i <= l) and (r <= j) then exit(t[k]);
	
	mid := (l + r) div 2;
	tmp1 := get(k*2, l, mid, i, j);
    tmp2 := get(k*2+1, mid+1, r, i, j);
    
    if tmp1.ans = oo then exit(tmp2);
    if tmp2.ans = oo then exit(tmp1);
    
    tmp1 := add(tmp1, tmp2);
	exit(tmp1);
end;
procedure process;
var i, x, y: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	for i := 1 to n do read(f, a[i]);
	buildIT(1, 1, n);
	
	readln(f, q);
	for i := 1 to q do 
	begin
		readln(f, x, y);
		writeln(get(1, 1, n, x, y).ans);
	end;
	close(f);
end;

begin
	process;
end.
