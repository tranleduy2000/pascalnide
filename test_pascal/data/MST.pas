const maxn = 10001; maxm = 100001;
type edge = record 
		u, v: integer;
		c: longint; end;
var
	e: array[1..maxm] of edge;
	lab: array[1..maxn] of longint;
	n, m: longint;
	
procedure sort(l,r: longint);
var k, i, j:longint;
	tmp: edge;
begin
	i := l; j := r; k := e[(l+ r) div 2].c;
	repeat
		while e[i].c < k do inc(i);
		while e[j].c > k do dec(j);
		if i <= j then
		begin
			tmp := e[i]; e[i] := e[j]; e[j] := tmp;
			inc(i); dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l, j);
end;

function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var	x : longint;
begin
	x := lab[r1] + lab[r2];
	if lab[r1] < lab[r2] then
	begin
		lab[r1] := x;
		lab[r2] := r1;	
	end else
	begin
		lab[r1] := r2;
		lab[r2] := x;
	end;
end;

procedure process;
var i, r1, r2, count: longint;
	k: int64;
	f: text;
begin
	assign(f, 'file.inp'); reset(f);
	readln(f, n, m);
	for i := 1 to m do readln(f, e[i].u, e[i].v, e[i].c);
	close(f);
	
	for i := 1 to n do lab[i] := -1;
	
	sort(1, m);
	count := 0;
	k := 0;
	for i := 1 to m do
	begin
		r1 := getroot(e[i].u); r2 := getroot(e[i].v);
		if r1 <> r2 then
		begin
			union(r1, r2);
			inc(count);
			k := k + e[i].c;
			if count = n - 1 then break;
		end;
	end;
	writeln(k);
end;

begin
	process;
end.
