var
	heap: array[1..100000] of longint;
	n: longint;

procedure swap(var x, y: longint);
var
	tmp: longint;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

procedure upHeap(v: longint);
begin
	if (v = 1) or (heap[v] < heap[v div 2]) then exit;
	swap(heap[v], heap[v div 2]);	
	upHeap(v div 2);
end;

procedure downHeap(v: longint);
var
	c: longint;
begin
	c := v * 2;
	if c > n then exit;
	if (c < n) and (heap[c] < heap[c+1]) then inc(c);
	if heap[v] < heap[c] then
	begin
		swap(heap[v], heap[c]);
		downHeap(c);
	end;
end;

procedure push(U: longint);
begin
	inc(n);
	heap[n] := u;
	upHeap(n);
end;

function pop(v: longint): longint;
begin
	pop := heap[v];
	heap[v] := heap[n];
	dec(n);
	upHeap(v);
	downHeap(v);
end;

var 
	cmd: string;
	
procedure print;
var
	i: longint;
begin
	for i := 1 to n do write(heap[i], ' ');
end;

begin
	n := 0;
	push(1443);
	push(542);
	push(1123);
	push(234);
	push(3461);
	push(2243);
	push(43);
	push(2);
	print;
end.
