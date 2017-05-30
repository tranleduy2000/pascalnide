const maxn = 70000; oo = 1000000;
type Pnode = ^Tnode;
	 TNode = record u: longint;	next: Pnode; end;
var n, q: longint;
	node: array[1..maxn] of Pnode;
	f: text;
	free: array[1..maxn] of boolean;
    depth: array[1..maxn] of longint;
    parent: array[1..maxn, 0..20] of longint;
	it: array[1..maxn*4 + 1] of longint;
		
procedure pushE(u, p: longint);
var t: pnode;
begin
	new(t);
	t^.u := u;
	t^.next := node[p];
	node[p] := t;
end;

procedure dfs(u, p: longint);
var t: pnode;
	v: longint;
begin
	t := node[u];
	while t <> nil do
	begin
		v := t^.u;
		if free[v] and (v <> p) then
		begin
			depth[v] := depth[u] + 1;
			free[v] := false;
			dfs(v, u);
			parent[v, 0] := u;
		end;
		t := t^.next;
	end;
end;

procedure buildLca;
var i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do parent[i, j] := parent[parent[i, j-1], j-1];
end;

function lca(u, v: longint): longint;
var del, j, tmp: longint;
begin
	if u = 0 then exit(v);
	if v = 0 then exit(u);
	
	if depth[u] < depth[v] then
	begin
		tmp := u;
		u := v;
		v := tmp;
	end;
	del := depth[u]- depth[v];
	for j := 0 to 20 do
		if del shr j and 1 = 1 then u := parent[u, j];
		
	if u = v then exit(u);
	
	for j := 20 downto 0 do
		if parent[u, j] <> parent[v, j] then
		begin
			u := parent[u, j];
			v := parent[v, j];
		end;
	u := parent[u, j];
	exit(u);
end;

procedure buildIt(k, l, r: longint);
var mid: longint;
begin
	if l = r then 
	begin
		it[k] := l;
		exit;
	end;
	mid := (l + r) div 2;
	buildIt(k*2, l, mid);
	buildIt(k*2 + 1, mid + 1, r);
	it[k] := lca(it[k*2], it[k*2 + 1]);
end;

function get(k, l, r, i, j: longint): longint;
var mid, tmp1, tmp2: longint;
begin
	if (j < l) or (i > r) then exit(0);
	if (i <= l) and (r <= j) then exit(it[k]);
	mid := (l +r) div 2;
	tmp1 := get(k*2, l, mid, i, j);
	tmp2 := get(k*2 + 1, mid + 1, r, i, j);
	exit(lca(tmp1, tmp2));
end;

procedure process;
var i, u, v: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, q);
	for i := 1to n do node[i] := nil;
	for i := 1 to n - 1 do
	begin
		readln(f, u, v);
		pushE(u, v);
		pushE(v, u);
	end;
	fillchar(free, sizeof(free), true);
	depth[1] := 1;
	dfs(1, 0);
	buildLCA;
	buildIT(1, 1, n);
	
	for i := 1 to q do
	begin
		readln(f, u, v);
		writeln(get(1, 1, n, u, v));
	end;
	close(f);
end;

BEGIN
	process;
END.
	
