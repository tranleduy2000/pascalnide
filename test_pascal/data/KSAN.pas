var
	n: longint;
	a, b: array[1..100000] of int64;
	heap: array[1..100000] of int64;
	nheap: longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do read(a[i]);
	for i := 1 to n do read(b[i]);
end;

procedure swap(var x, y: int64);
var
	tmp: int64;
begin
	tmp := x;
	x := y;	 
	y := tmp;
end;

procedure upheap(i: longint);
begin
	if i = 1 then exit;
	if heap[i] <= heap[i div 2] then
	begin
		swap(heap[i], heap[i div 2]);
		upheap(i div 2);
	end;
end;

procedure downheap(i: longint);
var
	c: longint;
begin
	if i * 2 > nheap then exit;
	c := i * 2;
	if (c < nheap) and (heap[c] > heap[c+1]) then inc(c);
	if heap[i] > heap[c] then
	begin
		swap(heap[i], heap[c]);
		downheap(c);
	end;
end;

procedure push(v: longint);
begin
	inc(nheap);
	heap[nheap] := v;
	upheap(nheap);
end;

function pop: longint;
begin
	if nheap = 0 then exit;
	pop := heap[1];
	heap[1] := heap[nheap];
	dec(nheap);
	downheap(1);
end;

procedure sort(l, r: longint);
var
	k1, k2, i, j: longint;
begin
	i := l;
	j := r;
	k1 := a[(l + r) div 2];
	k2 := b[(l + r) div 2];
	repeat
		while (a[i] < k1) or ((a[i] = k1) and (b[i] < k2)) do inc(i);
		while (a[j] > k1) or ((a[j] = k1) and (b[j] > k2)) do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			swap(b[i], b[j]);
			inc(i);
			dec(j);
		end;
	until i > j;
	if i < r then sort(i, r);
	if l < j then sort(l, j);
end;

procedure init;
begin
	readf;
	nheap := 0;
	sort(1, n);
end;

procedure print;
var
	i: longint;
begin
	for i := 1 to nheap do write(heap[i], ' '); writeln;
end;

procedure process;
var
	count, i: longint;
	v: longint;
begin
	count := 1;
	push(a[1] + b[1]);
	for i := 2 to n do
	begin
		v := heap[1];
	//	writeln('nheap = ', nheap); print;
		if (v <= a[i]) then 
		begin
			pop;
			push(a[i] + b[i]);
		end
		else
		begin
			inc(count);
			push(a[i] + b[i]);
		end;
	end;
	writeln(count);
end;

BEGIN
	init;
	process; //out
END.
