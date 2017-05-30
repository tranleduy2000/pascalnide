var
	n, nheap, top: longint;
	stack, heap, a: array[1..100000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do readln(a[i], heap[i]);
end;

procedure swap(var x, y: longint);
var
	tmp: longint;
begin
	tmp := x;
	x := y;
	y := tmp;
end;

procedure sort(l ,r : longint);
var
	i, j, k: longint;
begin
	i := l;
	j := r;
	k := a[(l + r) div 2];
	repeat
		while a[i] > k do inc(i);
		while a[j] < k do dec(j);
		if i <= j then
		begin
			swap(a[i], a[j]);
			inc(i);
			dec(j);
		end;
	until i > j;
end;

procedure upheap(i: longint);
begin
	if (i = 1) or (heap[i] < heap[i div 2]) then exit;
	swap(heap[i], heap[i div 2]);
	upheap(i div 2);
end;

procedure downheap(i: longint);
var
	c: longint;
begin
	c := i * 2;
	if i > nheap then exit;
	if (c < nheap) and (heap[c] < heap[c + 1]) then inc(c);
	if heap[i] < heap[c] then
	begin
		swap(heap[i], heap[c]);
		downheap(c);
	end;
end;

procedure pushheap(v: longint);
begin
	inc(nheap);
	heap[nheap] := v;
	upheap(nheap);
end;

function popheap: longint;
begin
	popheap := heap[1];
	heap[1] := heap[nheap];
	dec(nheap);
	downheap(1);
end;

procedure pushstack(v: longint);
begin
	inc(top);
	stack[top] := v;
end;

procedure resetstack;
begin
	while top > 0 do
	begin
		pushheap(stack[top]);
		dec(top);
	end;
end;

procedure process;
var
	i, v, res, j: longint;			
begin
	top := 0;
	nheap := n;
	res := 0;
	sort(1, n);
	for i := 1 to n div 2 do downheap(i);
	for i := 1 to nheap do write(heap[i], ' '); writeln;
	for i := 1 to n do
	begin
		v := popheap();
		//writeln('v = ', v);
		while (v = a[i]) and (nheap > 0) do
		begin
			writeln('asd');
			pushstack(v);
			v := popheap;
		end;
		res :=res + abs(a[i] - v);
		//writeln;
		writeln('result: ', a[i], ' ', v);
		resetstack;
		write('heap: '); for j := 1 to nheap do write(heap[j], ' '); writeln;
	end;
	writeln(res);
end;

begin
	readf;
	process;
end.
