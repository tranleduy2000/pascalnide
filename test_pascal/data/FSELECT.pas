const maxn = 200111;
type Pnode = ^Tnode;
	 TNode = record
		u: longint;
		next: Pnode;
	 end;
	 
var	meet: array[0..maxn div 2] of Pnode;
	e: array[0..maxn] of Pnode;
	parent: array[0..maxn, 0..20] of longint;
	depth: array[0..maxn] of longint;
	n, k, root, count:longint;
	qx, qy: array[0..maxn] of longint;
	
procedure pushE(u, p: longint);
var t: pnode;
begin
	new(t);
	t^.u := u;
	t^.next := e[p];
	e[p] := t;
end;

procedure pushM(index, u: longint);
var t: pnode;
begin
	new(t);
	t^.u := u;
	t^.next := meet[index];
	meet[index] := t;
end;

procedure init;
var i, v, u: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, k);
	root := 1;
	for i := 1 to n do e[i] := nil;
	for i := 1 to n do
	begin
		readln(f, u, v);
		if v = 0 then root := i
		else pushE(i, v);
		pushM(u, i);
	end;
	for i := 1 to k do readln(f, qx[i], qy[i]);
	close(f);
	depth[root] := 1;
	count := 0;
end;

procedure dfs(u, p: longint);
var t: pnode;
	v : longint;
begin
	t := e[u];
	while t <> nil do
	begin
		v := t^.u;
		if (v <> p) then
		begin
			depth[v] := depth[u] + 1;
			dfs(v, u);
			parent[v, 0] := u;
		end;
		t:= t^.next;
	end;
end;

procedure buildLca;
var i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
			parent[i, j] := parent[parent[i, j-1], j-1];
end;

function min(x, y: longint): longint;
begin if x < y then exit(X) else exit(y);end;

function max(x, y: longint): longint;
begin	if x > y then exit(X) else exit(y); end;

function lca(u, v: longint): longint;
var del, j: longint;
begin
	del := depth[v] - depth[u];
	for j := 0 to 20 do	
		if del shr j and 1 = 1 then v := parent[v, j];
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

procedure process;
var  v, i, maxNode, res: longint;
	 t: pnode;
begin
	for i := 1 to k do
	begin
		//find min depth
		t := meet[i];
		maxNode := t^.u;
		while t <> nil do
		begin			
			v := t^.u;
			if depth[v] > depth[maxNode] then maxNode := v;
			t:= t^.next;
		end;
		//process
		t := meet[i];
		res := 0;
		while t <> nil do
		begin
			v := lca(t^.u, maxNode); 
			res := max(res, depth[t^.u] + depth[maxNode] - 2 * depth[v]);
			t := t^.next;
		end;
		writeln(res);
	end;
end;

begin	
    init;
	dfs(root, 0);
	buildLca;
	process;
end.
