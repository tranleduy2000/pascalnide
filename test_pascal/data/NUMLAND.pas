const maxn = 100000; yes = 'war again'; no = 'peace';
type Pnode= ^Tnode;
	 Tnode = record
		u: longint;
		next: Pnode;
	 end;
var e: array[1..maxn] of pnode;
	depth, qx, qy, qi, qj: array[1..maxn] of longint;
	parent: array[1..maxn , 0..20] of longint;
	free: array[1..maxn] of boolean;
	n, q: longint;
procedure pushE(u, p: longint);
var t: pnode;
begin
	new(t);
	t^.u := u;
	t^.next := e[p];
	e[p] := t;
end;

procedure input;
var i, u, v: longint;
begin
	readln(n, q);
	for i := 1 to n do e[i] := nil;
	for i :=1 to n -1 do
	begin
		readln(u, v);
		pushE(u, v);
		pushE(v, u);
	end;
	for i := 1 to q do readln(qx[i], qy[i], qi[i], qj[i]);
end;

procedure dfs(u, p: longint);
var t: pnode;
	v: longint;
begin
	t := e[u];
	while t <> nil do 
	begin
		v := t^.u;
		if free[v] and (v <> p) then
		begin
			free[v] := false;
			depth[v] := depth[u] + 1;
			dfs(v, u);
			parent[v, 0] := u;
		end;
		t := t^.next;
	end;
end;

procedure init;
var i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
			parent[i, j] := parent[parent[i, j-1], j-1];
end;
function lca(u, v: longint): longint;
var del, i, tmp, j: longint;
begin
	if depth[u] < depth[v] then 
	begin
		tmp := u;
		u := v;
		v := tmp;
	end;
	del := depth[u] -depth[v];
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

procedure process;
var i, r1, r2: longint;
begin
	input;
	fillchar(free, sizeof(free), true);
	depth[1] := 1;
	dfs(1, 0);
	init;
	for i := 1 to q do
	begin
		r1 := lca(qx[i], qy[i]);
		r2 := lca(qi[i], qj[i]);
		//writeln('lca ', qx[i], ';', qy[i], ' = ', r1, '   lca ', qi[i], ';', qj[i], ' = ', r2);
		if depth[r1] < depth[r2] then
		begin
			if (lca(qx[i], r2) = r2) or (lca(qy[i], r2) = r2) then 
			writeln(yes) else writeln(no)
		end else
		begin
			if (lca(qi[i], r1) = r1) or (lca(qj[i], r1) = r1) then 
			writeln(yes) else writeln(no)  
		end;
	end;
end;

begin
	process;
end.
