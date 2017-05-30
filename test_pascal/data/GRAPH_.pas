const maxn = 10000; maxe = 100000;
var a: array[1..maxn, 1..maxn] of boolean;
	num, low, parent: array[1..maxn] of longint;
	n, m, count: longint;
procedure input;
var f: text;
	i, u, v: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, m);
	fillchar(a, sizeof(a), false);
	for i :=1  to m do
	begin
		readln(f, u, v);
		a[u, v] := true;
		a[v, u] := true;
	end;
	close(f);
end;

procedure dfs(u: longint);
var v: longint;
begin
	inc(count);
	num[u] := count;
	low[u] := n + 1;
	for v := 1 to n do
		if a[u, v] then
		begin
			a[v, u] := false;
			if parent[v] = 0 then
			begin
				parent[v] := u;
				dfs(v);
				if low[u] > low[v] then low[u] := low[v];
			end else
				if low[u] > num[v] then low[u] := num[v];
		end;
end;

procedure process;
var u: longint;
begin
	fillchar(parent, sizeof(parent), 0);
	count := 0;
	for u := 1 to n do
		if parent[u] = 0 then
		begin
			parent[u] := -1;
			dfs(u);
		end;
end;

procedure output;
var u, v, cntB, cntC: longint;
	nChild: array[1..maxn] of longint;
	isCut: array[1..maxn] of boolean;
begin
	cntB := 0;
	cntC := 0;
	fillchar(nChild, sizeof(nChild), 0);
	for u := 1 to n do
		if parent[u] <> -1 then inc(nChild[parent[u]]);
	
	
	//cut vertices
	fillchar(isCut, sizeof(isCut), false);
	for v := 1 to n do
		if parent[v] <> -1 then
		begin
			u := parent[v];
			if (low[v] >= num[u]) then 
				isCut[u] := isCut[u] or (parent[u] <>-1) or (nChild[u] >= 2);
		end;
		
	for v := 1 to n do 
		if isCut[v] then inc(cntC);
	write(cntC, ' ');
	
	for v := 1 to n do
	begin
		u := parent[v];
		if (u <> -1) and (low[v] >= num[v]) then inc(cntB);
	end;
	writeln(cntB);
end;

begin
	input;
	process;
	output;
end.
