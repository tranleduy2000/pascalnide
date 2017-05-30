const maxn = 4000000;
type edge = record u, v, c:longint; end;
	 node = record x, y, z, num: longint; end;
	 
var e: array[1..maxn] of edge;
	a: array[1..maxn] of node;
	n, count: longint;
	lab: array[1..100000] of longint;
	
procedure swap(var a, b: node);
var tmp:node;
begin
	tmp := a;
	a := b;
	b := tmp;
end;

procedure sortx(l, r: longint);
var i, j, k: longint;
begin
	k := a[(l + r) div 2].x;
	i := l;
	j := r;
	repeat
		while a[i].x < k do inc(i);
		while a[j].x > k do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			dec(j); inc(i);
		end;
	until i > j;
	if i < r then sortx(i, r);
	if l < j then sortx(l, j);
end;


procedure sorty(l, r: longint);
var i, j, k: longint;
begin
	k := a[(l + r) div 2].y;
	i := l;
	j := r;
	repeat
		while a[i].y < k do inc(i);
		while a[j].y > k do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			dec(j); inc(i);
		end;
	until i > j;
	if i < r then sorty(i, r);
	if l < j then sorty(l, j);
end;


procedure sortz(l, r: longint);
var i, j, k: longint;
begin
	k := a[(l + r) div 2].z;
	i := l;
	j := r;
	repeat
		while a[i].z < k do inc(i);
		while a[j].z > k do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			dec(j); inc(i);
		end;
	until i > j;
	if i < r then sortz(i, r);
	if l < j then sortz(l, j);
end;

procedure sort(l, r: longint);
var i, j, k: longint;
	tmp: edge;
begin
	k := e[(l + r) div 2].c;
	i := l;
	j := r;
	repeat
		while e[i].c < k do inc(i);
		while e[j].c > k do dec(j);
		if i <= j then
		begin
			tmp := e[i];
			e[i] := e[j];
			e[j] := tmp;
			dec(j); inc(i);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l, j);
end;

procedure input;
var i: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	
	for i := 1 to n do with a[i] do 
	begin
		readln(f, x, y, z);
		a[i].num := i;
	end;
	close(f);
	
	count := 0;
	sortx(1, n);
	for i := 1 to n-1 do
	begin
		inc(count);
		e[count].u := a[i].num;
		e[count].v := a[i+1].num;
		e[count].c := abs(a[i+1].x - a[i].x);
		//writeln('x ', e[i].u, ' ', e[i].v, ' ', e[i].c);
	end;
	
	sorty(1, n);
	for i := 1 to n-1 do
	begin
		inc(count);
		e[count].u := a[i].num;
		e[count].v := a[i+1].num;
		e[count].c := abs(a[i+1].y - a[i].y);
	//writeln('y ', e[count].u, ' ', e[count].v, ' ', e[count].c);
	end;
	
	sortz(1, n);
	for i := 1 to n-1 do
	begin
		inc(count);
		e[count].u := a[i].num;
		e[count].v := a[i+1].num;
		e[count].c := abs(a[i+1].z - a[i].z);		
	//	writeln('z ', e[count].u, ' ', e[count].v, ' ', e[count].c);
	end;
	
	sort(1, count);
end;

function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var x: longint;
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

procedure kruskal;
var i, r1, r2, cnt, cost: longint;
begin
	//writeln;
	//for i := 1 to count do write(e[i].c, ' ');
	//writeln; 
	//writeln('Count = ', count);
	for i := 1 to n do lab[i] := -1;
	cnt := 0;
	cost := 0;
	for i := 1 to count do
	begin
		r1 := getroot(e[i].u); r2 := getroot(e[i].v);
		if r1 <> r2 then
		begin
			inc(cnt);
			union(r1, r2);
			cost := cost + e[i].c;
	//		writeln(e[i].u, ' ', e[i].v, ' ', e[i].c);
			if cnt = n - 1 then break;
		end;
	end;
	writeln(cost);
end;

begin	
	input;
	kruskal;
end.

