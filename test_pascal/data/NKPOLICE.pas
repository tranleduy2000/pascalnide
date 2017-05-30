const maxn = 100000; maxe = 500000;
type pnode= ^Tnode;
	 TNode = record
		u, id: longint;
		next: Pnode;
	 end;
	
var f, fo: text;
	e: array[1..maxn] of pnode;
	nChild, num, low, depth, leave: array[1..maxn] of longint;
	count, n, q: longint;
	parent: array[1..maxn, 0..20] of longint;
	free: array[-maxe..maxe] of boolean;
	isCut: array[1..maxn] of boolean;

function min(x, y: longint): longint;	
begin
	if x < y then exit(x) else exit(y);
end;
procedure pushE(u, p, id: longint);
var t: pnode;
begin
	new(t);
	t^.u := u;
	t^.id := id;
	t^.next := e[p];
	e[p] := t;
end;

procedure init;
begin
	fillchar(nChild, sizeof(nChild), 0);
	fillchar(isCut, sizeof(isCut), false);
	fillchar(free, sizeof(Free), true);
end;

procedure input;
var u,m, v, i: longint;
begin
	assign(f, 'file.inp');	reset(f);
	assign(fo, 'file.out'); rewrite(fo);
	readln(f, n, m);
	init;
	for i := 1 to m do
	begin
		readln(f, u, v);
		pushE(u, v, i);
		pushE(v, u, -i);
	end;
	
end;

procedure visit(u: longint);
var t: pnode;
	v, id: longint;
begin
	inc(count); low[u] := count; num[u] := count;
	t := e[u];
	while t <> nil do
	begin
		id := t^.id;
		if free[id] then //
		begin
			free[-id] := false;//da dinh chieu canh [u, v] thanh cung (u, v)
			v := t^.u;
			if num[v] > 0 then //duyet qua v roi
				low[u] := min(low[u], num[v])
			else
			begin
				inc(nChild[u]);
				depth[v] := depth[u] + 1;
				visit(v);
				parent[v, 0] := u;
				if (low[v] >= num[u]) and not isCut[u] then	isCut[u] := true;  //u la khop
				//xet cau [brigde] if low[v] > low[u] -> (u, v) la cau
				low[u] := min(low[u], low[v]);
			end;
		end;
		t := t^.next;
	end;
	leave[u] := count;
end;

procedure dfs(u, p: longint);
var t: pnode;
	v, id: longint;
begin
	t := e[u];
	while t <> nil do
	begin
		id := t^.id;
		if free[id] then //
		begin
			free[-id] := false;//da dinh chieu canh [u, v] thanh cung (u, v)
			v := t^.u;
			if num[v] > 0 then //duyet qua v roi
				low[u] := min(low[u], num[v])
			else
			begin
				inc(nChild[u]);
				depth[v] := depth[u] + 1;
				visit(v);
				parent[v, 0] := u;
				if (low[v] >= num[u]) and not isCut[u] then	isCut[u] := true;  //u la khop
				//xet cau [brigde] if low[v] > low[u] -> (u, v) la cau
				low[u] := min(low[u], low[v]);
			end;
		end;
		t := t^.next;
	end;
end;

procedure buildLCA;
var i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do parent[i, j] := parent[parent[i, j-1], j-1];
end;

procedure swap(var x, y: longint);
var tmp:  longint;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

function lca(u, v: longint): longint;
var del, j: longint;
begin
	if depth[u] < depth[v] then swap(u, v);
	del := depth[u] - depth[v];
	for j := 0 to 20 do if del shr j and 1 = 1 then u := parent[u, j];
	if u =v then exit(u);
	for j := 20 downto 0 do
		if parent[u, j] <> parent[v, j] then
		begin
			u := parent[u, j];
			v := parent[v, j];
		end;
	u := parent[u, j];
	exit(u);
end;

function isBirdge(u, v: longint): boolean; //u cha, v con
begin
	if (low[v] > num[u]) then exit(true);
	exit(false);
end;

function isParent(u, v: longint): boolean; //u cha, v con
begin
	if (num[u] <= num[v]) and (leave[u] >= leave[v])  then exit(true);
	exit(false);
end;

function answer2: boolean;
var u, v, i, j, cmd, x, y, c,r1, r2: longint;
	b1, b2, upper2, upper1: boolean;
begin
	readln(f, u, v, c);
	
	if not isCut[c] then exit(true); //ko la khop ??
	
	b1 := isParent(c, u);
	b2 := isParent(c, v);
	//writeln(b1, ' ', b2);
	//neu ko la cha cua thang nao thi bo cung chang sao
	if not (b1 or b2) then exit(true);

	r1 := lca(c, u);
	r2 := lca(c, v);
	///writeln('lca ', r1, ' ', r2);
	upper1 := depth[r1] < depth[c];
	upper2 := depth[r2] < depth[c];
	//writeln('upper ', upper1, ' ', upper2);
	if b1 and b2 then
	begin
		if upper1 and upper2 then exit(true);
		if r1 = r2 then exit(true);
		//lca(u, c) = lca(v, c) => truong hop ca 2 child o duoi parent  
		exit(false); //lca(u, c) <> lca(v, c) ma u, v deu nhan c la cha => 2 nhanh con
	end else
	if b1 then exit(upper1)
	else if b2 then exit(upper2);
end;

function answer1: boolean;
var u, v, x, y: longint;
	b1, b2: boolean;
begin
	readln(f, u, v, x, y);
	
	if not isBirdge(y, x) or not isBirdge(x, y) then exit(true);
		
	b1 := isParent(u, x);
	b2 := isParent(u, y);
	if (b1 and b2) or (not b1 and not b2) then exit(true);
	exit(false);	
end;
procedure process;
var u, v, i, j, cmd, x, y, c,r1, r2: longint;
	b1, b2: boolean;
begin
	input;
	depth[1] := 1;
	visit(1);
	if isCut[1] and (nChild[1] < 2) then isCut[1] := false;
	buildLCA;
	readln(f, q);
	for i := 1 to q do
	begin
		read(f, cmd);
		if cmd =1  then b1 := answer1 
		else b1 := answer2;
		if b1 then writeln(fo, 'yes') else writeln(fo, 'no');
	end;
	close(f);
	close(fo);
end;

begin
	process;
end.
