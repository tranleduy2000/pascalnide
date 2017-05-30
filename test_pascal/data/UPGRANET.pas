const maxn = 100000;
type Edge = record u, v, c: longint; mark: boolean; end;
	 Pnode = ^Tnode;
	 Tnode = record	u, c: longint;	next: pnode; end;
	 Data = record root, minC: longint; end;

var e: array[1..maxn] of edge;
	n, m: longint;
	depth, lab: array[1..maxn] of longint;
	parent: array[1..maxn, 0..20] of data;
	node: array[1..maxn] of Pnode;
	free: array[1..maxn] of boolean;
	
procedure swap(var x, y: edge);
var	 tmp: edge;
begin
	tmp := x; x := y ; y := tmp;
end;

procedure sort(l, r: longint);
var	k, i, j: longint;
begin
	i := l; j := r; k := e[(l + r) div 2].c;
	repeat
		while e[i].c > k do inc(i);
		while e[j].c < k do dec(j);
		if i <= j then
		begin	
			swap(e[i], e[j]);
			inc(i); dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l ,j);
end;

function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var tmp: longint;
begin
	tmp := lab[r1] + lab[r2];
	if lab[r1] < lab[r2] then
	begin
		lab[r1] := tmp;
		lab[r2] := r1;
	end else
	begin
		lab[r2] := tmp;
		lab[r1] := r2;
	end;
end;

procedure pushEdge(u, p, c: longint);
var t: pnode;
begin
	new(t);
	t^.c := c;
	t^.u := u;
	t^.next := node[p];
	node[p] := t;
end;


procedure kruskal;
var
	i, r1, r2, count: longint;
begin
	for i := 1 to m do e[i].mark := false;
	for i := 1 to n do lab[i] := -1;
	count := 0;
	for i := 1 to m do
	begin
		r1 := getroot(e[i].u);
		r2 := getroot(e[i].v);
		if r1 <> r2 then
		begin
			union(r1, r2);
			inc(count);
			e[i].mark := true;
			
			pushEdge(e[i].u, e[i].v, e[i].c);
			pushEdge(e[i].v, e[i].u, e[i].c);
			
			if count = n- 1 then exit;
		end;
	end;
end;


procedure dfs(u, p: longint);
var t: pnode; 
	v: longint;
begin
	t := node[u];
	while (t <> nil) do
	begin	
		v := t^.u;
		if free[v] and (t^.u <> p) then
		begin
			depth[v] := depth[u] + 1;
			free[v] := false;
			dfs(v, u);
			parent[v, 0].root := u;
			parent[v, 0].minC := t^.c;
		end;
		t := t^.next;
	end;
end;

function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;

procedure buildLca;
var i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
		begin
			parent[i, j].root := parent[parent[i, j-1].root, j-1].root;
			parent[i, j].minC := min( parent[i, j-1].minC,
							parent[parent[i, j-1].root, j-1].minC );
		end;	
end;

function lca(u, v: longint): longint;
var tmp, delta, minE, j: longint;
begin
	if depth[u] < depth[v] then
	begin	
		tmp := u;
		u := v;
		v := tmp;
	end; //ok
	
	minE := high(longint);
	delta := depth[u] - depth[v];
	
	for j := 0 to 20 do
		if (delta shr j) and 1 = 1 then
		begin
			minE := min(minE, parent[u, j].minC);
			u := parent[u, j].root;
		end;
		
	if u = v then exit(minE);
	
	for j := 20 downto 0 do
		if parent[u, j].root <> parent[v, j].root then
		begin
			minE := min(minE, min( parent[u, j].minC, parent[v, j].minC ) );
			u := parent[u, j].root;
			v := parent[v, j].root;
		end;
	minE := min(minE, min( parent[u, j].minC, parent[v, j].minC ) );
	exit(minE);
end;

procedure process;
var i, minE: longint;
	count : int64;
	f: text;
begin
	//inp
	assign(f, 'file.inp'); reset(f);
	readln(f, n, m);
	for i := 1 to m do with e[i] do readln(f , u, v, c);
	close(f);
	
	for i := 1 to n do node[i] := nil;
	fillchar(free, sizeof(free), true);
	
	//minimum span tree
	sort(1, m);
	kruskal;
	
	//lca
	depth[1] := 1;
	dfs(1, 0);
	buildLCA;
	
	//process
	count := 0;
	for i := 1 to m do
		if not e[i].mark then
		begin
			minE := lca(e[i].u, e[i].v);
			count := count + minE - e[i].c; 
		end;
	writeln(count);
end;

begin
	process;
end.
	
