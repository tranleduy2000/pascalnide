const maxn = 20001;
var
	a: array[1..maxn] of longint;
	heap: array[1..maxn] of qword;
	n, nheap: longint;
	
procedure swap( x, y: qword);
var
	tmp: longint;
begin
	tmp :=heap[x];
	heap[x] := heap[y];
	heap[y] := tmp;
end;
	
procedure upheap(u: longint);
begin
	if u =  1 then exit;
	if heap[u div 2] > heap[u] then
	begin
		swap(u div 2, u);
		upheap(u div 2);
	end;
end;

procedure downheap(u: longint);
var
	c: longint;
begin
	c := u * 2;
	if c > nheap then exit;
	if (c < nheap) and (heap[c] > heap[c+1]) then inc(c);
	if heap[u] > heap[c] then
	begin
		swap(u, c);
		downheap(c);
	end;
end;

function pop: longint;
begin
	pop := heap[1];
	heap[1] := heap[nheap];
	dec(nheap);
	downheap(1);
end;

	
procedure push(v: qword);
begin
	inc(nheap);
	heap[nheap] := v;
	upheap(nheap);
end;


procedure answer;
var
	res: qword;
	t1, t2: qword;
begin
	res := 0;
	while nheap >= 2 do
	begin
		t1 := pop;
		t2 := pop;
		res := res + (t1 + t2);
		push(t1 + t2);
	end;
	writeln(res);
end;

procedure process;
var
	f: text;
	i, t, j: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, t);
	for i := 1 to t do
	begin
		readln(f, n);
		nheap := 0;
		for j := 1 to n do
		begin
			read(f, a[j]);
			push(a[j]);
		end;
		answer;
	end;
	close(f);
end;
begin
	process;
end.
