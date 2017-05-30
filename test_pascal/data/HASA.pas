var
	n: longint;
	heap: array[0..100000] of int64;

procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do read(heap[i]);
end;

procedure swap(var x, y: int64);
var
	tmp: int64;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

procedure downheap(root: longint);
var
	child: longint;
begin
	child := root * 2;
	if child > n then exit;
	if (child < n) and (heap[child] >= heap[child + 1]) then inc(child);
	if heap[root] > heap[child] then
	begin
		swap(heap[root], heap[child]);
		downheap(child);
	end;
end;

procedure upheap(child: longint);
begin
	if (child = 1) or (heap[child] > heap[child div 2]) then exit;
	swap(heap[child], heap[child div 2]);
	upheap(child div 2);
end;

procedure push(v: int64);
begin
	inc(n);
	heap[n] := v;
	upheap(n);
end;

procedure pop(i: longint);
begin
	heap[i] := heap[n];
	dec(n);
	upheap(i);
	downheap(i);
end;

procedure initheap;
var
	i: longint;
begin
	for i := n div 2 downto 1 do downheap(i);
end;

procedure process;
var
	x, y, res: int64;
begin
	res := 0;
	while n > 1 do
	begin
		x := heap[1]; pop(1);
		y := heap[1]; pop(1);
		res := res + x + y;
		push(x + y);
	end;
	writeln(res);
end;

begin
	readf;
	initheap;
	process;
end.
