const maxn = 10000;
type Pnode = ^TNode;
	 TNode = record
		u: longint;
		next: PNode;
	end;
var e: array[1..maxn] of PNode;
	parent: array[1..maxn, 0..20] of longint;
	depth: array[1..maxn] of longint;
	n: longint;
	
procedure init;
var u: longint;
begin
	for u := 1 to n do e[u] := nil;
end;

procedure pushE(v, u: longint);
var	p: pnode;
begin
	new(p);
	p^.u := v;
	p^.next := e[u];
	e[u] := p;
end;

procedure dfs(u, p: longint);
var node: Pnode;
	v: longint;
begin
	node := e[u];
	while node <> nil do 
	begin
		v := node^.u;
		depth[v] := depth[u] + 1;
		dfs(v, u);
		parent[v, 0] := u;
		node := node^.next;
	end;
end;

procedure swap(var	x, y: longint);
var tmp: longint;
begin
	tmp := x; x := y; y := tmp;
end;

function getbit(v, j: longint): byte;
begin
	exit((v shr j) and 1);
end;

function lca(u, v: longint): longint;
var	delta: longint;
	j: longint;
begin
	if depth[u] < depth[v] then swap(u, v);
	delta := depth[u] - depth[v];
	for j := 0 to 20 do
		if getbit(delta, j) = 1 then u := parent[u, j];
	if u = v then exit(u);
	for j := 20 downto 0 do
		if parent[u, j] <> parent[v, j] then
		begin
			u := parent[u, j];
			v := parent[v, j];
		end;		
	u := parent[u, j];
	v := parent[v, j];
	exit(u);
end;

procedure buildLCA;
var j, i :longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
			parent[i, j] := parent[parent[i, j-1], j-1];
end;

procedure process;
var
	t, u, v, q, tt, i, k: longint;
	f: text;
begin
	assign(f, 'file.inp'); reset(f);
	readln(f, tt);
	for t := 1 to tt do
	begin
		writeln('Case ', t, ':');
		readln(f, n);
		init;
		for u := 1 to n do
		begin
			read(f, k);
			for i := 1 to k do
			begin
			  read(f, v);
			  pushE(v, u);
			end;
		end;
		dfs(1, 0);
		readln(f, q);
		buildLCA;
		for i := 1 to q do
		begin
			readln(f, u, v);
			writeln(lca(u, v));
		end;
	end;
	close(f);
end;

begin
	process;
end.
