const maxn = 1000;
type
	TNode = ^Pnode;
	Pnode = record
		u: longint;
		next: Tnode;
	end;

var e: array[0..maxn] of Tnode;
	f: text;
	parent: array[0..maxn, 0..20] of longint;
	depth: array[0..maxn] of longint;
	n: longint;
	
procedure push(u, p: longint);
var tmp: tnode;
begin
	new(tmp);
	tmp^.u := u;
	tmp^.next := e[p];
	e[p] := tmp;
end;

procedure loadGraph;
var
	u, v, j, k: longint;
begin
	readln(f, n);	
	for j := 1 to n do e[j] := nil;
	fillchar(depth, sizeof(depth), 0);
	for u := 1 to n do
	begin
		read(f, k) ;
		for j := 1to k do
		begin
			read(f, v);
			push(v, u);
		end;
	end;
end;


procedure dfs(u, p: longint);
var 	
	v: longint;
	next: tnode;
begin
	next := e[u];
	while next <> nil do
	begin
		v := next^.u;
		if (depth[v] = 0) and (v <> p) then
		begin
			depth[v] := depth[u] + 1;
			parent[v, 0] := u;
			dfs(v, u);
		end;
		next := next^.next;
	end;
end;

procedure buildLCA;
var 
	i, j: longint;
begin
	for i := 1 to n do
		for j := 1 to 20 do
			parent[i, j] := parent[parent[i, j-1], j-1];
end;

function lca(u, v: longint): longint;
var
	j, tmp, delta: longint;
begin
	if depth[u] < depth[v] then 
	begin
		tmp := u;
		u := v;
		v := tmp;
	end;
	delta := depth[u] - depth[v];
	for j := 0 to 20 do
		if (delta shr j) and 1 = 1 then u := parent[u, j];
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
var
	u, v, i, q,test, t: longint;
	
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, test);
	for t := 1 to test do
	begin
		loadGraph;
		depth[1] := 1;
		dfs(1, 0); //build depth
		buildLCA;
		readln(f, q);
		writeln('Case ',t,':');
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
