type
	edge = record	
		u, v: longint;
		c: longint;
		b: boolean;
	end;
var
	a: array[0..1000] of string;
	n, k: longint;
	t: longint;
	e: array[0..1000*1000] of edge;
	lab: array[0..1001] of longint;
	free: array[0..1000] of boolean;
	
procedure readf;
var
	i: longint;
begin
	readln(n, k);
	for i := 1to n do readln(a[i]);
end;

function eval(x1, x2: string): longint;
var
	count, i: longint;
begin
	count := 0;
	for i := 1 to length(x1) do if x1[i] <> x2[i] then inc(count);
	exit(count * 2);
end;

procedure loadgraph;
var
	u, v: longint;
begin
	t := 0;
	for u := 1 to n do
	begin
		inc(t);
		e[t].u := 0;
		e[t].v := u;
		e[t].c := length(a[u]) ;
		e[t].b := false;
	end;
	
	for u := 1 to n - 1 do
		for v := u + 1 to n do
		begin
			inc(t);
			e[t].u := u;
			e[t].v := v;
			e[t].c := eval(a[u], a[v]);
			e[t].b := false;
		end;
end;

function getroot(u: longint): longint;
begin
	while lab[u] > 0 do u := lab[u];
	getroot := u;
end;

procedure init;
var
	u: longint;
begin
	for u := 0 to n do lab[u] := -1;
end;

procedure union(r1, r2: longint);
var
	x: longint;
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

procedure sort(l, r: longint);
var
	i, j, k: longint;
	tmp: edge;
begin
	i := l;
	j := r;
	k := e[(l + r) div 2].c;
	repeat
		while e[i].c < k do inc(i);
		while e[j].c > k do dec(j);
		if i <= j then
		begin
			tmp := e[i];
			e[i] := e[j];
			e[j] := tmp;
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l, j);
end;

procedure krushkal;
var
	r1,r2: longint;
	i, count: longint;
begin
	count := 0;
	for i := 1 to t do
	begin
		r1 := getroot(e[i].u);
		r2 := getroot(e[i].v);
		if count = n then exit;
		if r1 <> r2 then
		begin
			if (r1 <> 0) and (r2 <> 0) then inc(count);
			union(r1, r2);
			e[i].b := true;
		end;
	end;
end;


procedure sort2(l, r: longint);
var
	i, j, k: longint;
	tmp: edge;
begin
	i := l;
	j := r;
	k := e[(l + r) div 2].u;
	repeat
		while e[i].u < k do inc(i);
		while e[j].u > k do dec(j);
		if i <= j then
		begin
			tmp := e[i];
			e[i] := e[j];
			e[j] := tmp;
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort2(i, r);
	if l < j then sort2(l, j);
end;

procedure result;
var
	i: longint;
	res: longint;
begin
	res := 0;
	fillchar(free, sizeof(free), true);
	//for i := 1 to t  do 	writeln(e[i].u, ' ', e[i].v, ' ' ,e[i].c);
	//writeln;
	free[0] := false;
	for i := 0 to t do 
		if e[i].b then 
		begin
		//	writeln(e[i].u, ' ', e[i].v, ' ' ,e[i].c);
			if (e[i].u = 0) then res := res + k
			else res := res + e[i].c;
			free[e[i].u] := false;
			free[e[i].v] := false;
		end;
	writeln(res);
	fillchar(free, sizeof(free), true);
	free[0] := false;
	sort2(1, t);
	for i := 1 to t do 
		if e[i].b then 
		begin
			writeln(free[e[i].u], ' ', free[e[i].v]);
			if free[e[i].v] and free[e[i].u] then
			begin
				writeln(e[i].u, ' ', e[i].v);
				free[e[i].u] := false;
				free[e[i].v] := false;
			end else
			begin
				if free[e[i].u] then
				begin
					writeln(e[i].v, ' ' , e[i].u);
					free[e[i].u] := false;
				end else
				begin
					writeln(e[i].u, ' ' , e[i].v);
					free[e[i].v] := false;
				end;
			end;
		end;
end;

begin
	readf;
	loadgraph;
	sort(1, t);
	init;
	krushkal;
	result;
end.

