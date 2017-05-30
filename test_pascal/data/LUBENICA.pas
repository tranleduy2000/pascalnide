const maxn = 10001; maxm = 100000;
type PNode = ^TNode;
	 Tnode = record u, c: longint; next: Pnode; end;
	 data = record r, minc, maxc: longint; 	 end;
	 
var e: array[1..100000] of Pnode;
	parent: array[1..100000, 0..20] of data;
	free: array[1..maxm] of boolean;
	depth: array[1..maxm] of longint;
	n: longint;
	
procedure pushE(child, parent, c: longint); //c: weight
var
	p: pnode;
begin
	new(p);
	p^.u := child;
	p^.next := e[parent];
	p^.c := c;
	e[parent] := p;
end;

procedure dfs(u, p: longint);
var T: pnode;
	v: longint;
begin
	T := e[u];
	while T <> nil do
	begin
		v := T^.u;
		if free[v] and (v <> p)then
		begin
			depth[v] := depth[u] + 1;
			dfs(v, u);
			parent[v, 0].r := u;
			parent[v, 0].minC := T^.c;
			parent[v, 0].maxC := T^.c;
		end;
		T := T^.next;
	end;
end;

function min(x, y: longint): longint;
begin 
	if x < y then exit(x) else exit(y); 
end;

function max(x, y: longint): longint;
begin 
	if x > y then exit(x) else exit(y);
end;

procedure buildLCA;
var	i, j: longint;
begin
	for j := 1 to 20 do
		for i := 1 to n do
		begin
			parent[i, j].r := parent[parent[i, j-1].r, j-1].r;	
			parent[i, j].minC := min( parent[i, j-1].minC, parent[parent[i, j-1].r, j-1].minC );
			parent[i, j].maxC := max( parent[i, j-1].maxC, parent[parent[i, j-1].r, j-1].maxC );
		end;
end;

function return(r, minc, maxc: longint): data;
var tmp: data;
begin
	tmp.r := r;
	tmp.minC := minC;
	tmp.maxC := maxC;
	exit(tmp);
end;

function lca(u, v: longint): data;
var delta, tmp, j, minE, maxE: longint;
begin
	//swap
	if depth[u] < depth[v] then	begin
		tmp := u; u := v; v := tmp;
	end;
	
	minE := high(longint); maxE := low(longint); //init
	
	delta := depth[u] - depth[v];
	if delta > 0 then
		for j := 0 to 20 do
			if (delta shr j) and 1 = 1 then
			begin 
				minE := min(minE, parent[u, j].minC);
				maxE := max(maxE, parent[u, j].maxC);
				u := parent[u, j].r;   
			end;
	
	if u = v then 
	begin
		exit(return(u, minE, maxE));
	end;
	
	for j := 20 downto 0 do
	begin
		if parent[u, j].r <> parent[v, j].r then
		begin
			minE := min( minE, min( parent[u, j].minC, parent[v, j].minC ) );
			maxE := max( maxE, max( parent[u, j].maxC, parent[v, j].maxC ) );
			u := parent[u, j].r;
			v := parent[v, j].r;
		end;
	end;
	minE := min( minE, min( parent[u, j].minC, parent[v, j].minC ) );
	maxE := max( maxE, max( parent[u, j].maxC, parent[v, j].maxC ) );
	u := parent[u, j].r;
	v := parent[v, j].r;
	exit(return(u, minE, maxE));
end;

procedure process;
var i, u,v, c, q: longint;
	f: text;
	tmp: data;
begin
	assign(f, 'file.inp'); reset(f);
	readln(f, n);
	for i := 1 to n do e[i] := nil;
	
	for i := 1 to n - 1 do
	begin
		readln(f, u, v, c);
		pushE(u, v, c);
		pushE(v, u, c);
	end;
	
	fillchar(free, sizeof(free), true);
	depth[1] := 1;
	dfs(1, 0);
	
	buildLca; //init
	
	readln(f, q);
	for i := 1 to q do
	begin
		readln(f, u, v);
		tmp := lca(u, v);
		writeln(tmp.minc, ' ', tmp.maxc) ;
	end;
end;

begin
	process;
end.
