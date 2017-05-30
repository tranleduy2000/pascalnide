const maxn = 3000; maxm = 100000;
type edge = record u, v, c: longint; mark: boolean; end;
	 pnode = ^tNode;
	 TNode = record
		u, c: longint;
		next: Pnode;
	 end;
	 pa = record root,maxC: longint; end;
	 
var e: array[1..maxm] of edge;
	lab: array[1..maxn] of longint;
	depth: array[1..maxn] of longint;
	parent: array[1..maxn, 0..20] of pa;
	node: array[1..maxn] of Pnode;
	f: text;
	n, m: longint;
	free: array[1..maxn] of boolean;
	
function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var x:  longint;
begin
	x := lab[r1] + lab[r2];
	if lab[r1] < lab[r2] then
	begin
		lab[r1] := x;
		lab[r2] := r1;
	end else
	begin
		lab[r2] := x;
		lab[r1] := r2;
	end;
end;

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
		while e[i].c < k do inc(i);
		while e[j].c > k do dec(j);
		if i <= j then
		begin	
			swap(e[i], e[j]);
			inc(i); dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l ,j);
end;

procedure readf;
var i: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, m);
	for i :=1  to m do with e[i] do readln(f, u, v, c);
end;

procedure pushE(child, parent, w: longint);
var t: pnode;
begin
	//writeln('push , ', child, ' ', parent);
	new(t);
	t^.u := child;
	t^.c := w;
	t^.next := node[parent];
	node[parent] := t;
end;

procedure init;
var i: longint;
begin
	for i := 1 to n do node[i] := nil;
	for i := 1 to n do lab[i] := -1;
	for i := 1 to m do e[i].mark := false;
	fillchar(free, sizeof(free), true);
	depth[1] := 1;
	sort(1, m);
end;

procedure kruskal;
var i, j, count, r2, r1: longint;
begin
	init;
	count := 0;
	for i := 1 to m do
	begin
		r1 := getroot(e[i].u); 
		r2 := getroot(e[i].v);
		if r1 <> r2 then
		begin
			inc(count);
			union(r1, r2);
			e[i].mark := false;
			pushE(e[i].u, e[i].v, e[i].c);
			pushE(e[i].v, e[i].u, e[i].c);
			if count = n -1 then exit;
		end;
	end;
end;

procedure dfs(u, p: longint);
var t: pnode;
	v: longint;
begin
	t := node[u];
	while t <> nil do
	begin
		v := t^.u;
		if free[v] and (v <> p)then
		begin
			free[v] := false;
			depth[v] := depth[u] + 1;
			dfs(v, u);
			parent[v, 0].root := u;
			parent[v, 0].maxC := t^.c;
		end;
		t := t^.next;
	end;
end;


function max(x, y: longint): longint;
begin
	if x > y then exit(x) else exit(y);
end;

procedure build;
var j, i: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
		begin
			parent[i, j].root := parent[parent[i, j-1].root, j-1].root;
			parent[i, j].maxC := max( parent[i, j-1].maxC, 
						parent[parent[i, j-1].root, j-1].maxC );
		end;
end;

function lca(u, v: longint): longint;
var i, d, maxE, j, tmp: longint;
begin
	if depth[u] < depth[v] then
	begin
		tmp := u;
		u := v;
		v := tmp;
	end;
	d := depth[u] - depth[v];
	maxE := low(longint);
	
	for j := 0 to 20 do
		if d shr j and 1 = 1 then 
		begin
			maxE := max(maxE, parent[u, j].maxC);
			u := parent[u, j].root;
		end;
		
	if u = v then exit(maxE);	
	
	for j := 20 downto 0 do
	begin
		if parent[u, j].root <> parent[v, j].root then
		begin
			maxE := max(maxE, max(parent[u, j].maxC, parent[v, j].maxC) );
			u := parent[u, j].root;
			v := parent[v, j].root;
		end;
	end;
	
	maxE := max(maxE, max(parent[u, j].maxC, parent[v, j].maxC) );
	exit(maxE);
end;

procedure process;
var	i, j, q, u, v: longint;
begin
	readf;
	kruskal;
	dfs(1, 0);
	build;
	//process 
	readln(f, q);
	for i := 1 to q do
	begin
		readln(f, u, v);
		writeln(lca(u, v));
	end;
	close(f);
end;

begin
	process;
end.
