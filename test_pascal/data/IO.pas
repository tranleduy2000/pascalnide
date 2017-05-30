var
	n, t: longint;
	heap,res: array[0..15001] of longint;
	
procedure swap(var x, y: longint);
var
	tmp: longint;
begin
	tmp := x;
	x := y;
	y := tmp;
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
	if c > n then exit;
	if (c < n) and (heap[c] < heap[c + 1]) then inc(c);
	if heap[i] < heap[c] then
	begin
		swap(heap[i], heap[c]);
		downheap(c);
	end;
end;

procedure push(v: longint);
begin
	if n > 15000 then exit;
	inc(n);
	heap[n] := v;
	upheap(n);
end;

procedure pop;
begin
	heap[1] := heap[n];
	dec(n);
	downheap(1);
end;

procedure process;
var
	c: string;
	v, heapmax, i: longint;
begin
	n := 0;
	while not eof do
	begin
		readln(c);
		if c = '' then break;
		if c[1] = '+' then
		begin
			delete(c, 1, 1);
			val(c, v, i);
			push(v);
		end else
		begin
			if n > 0 then
			begin
				heapmax := heap[1]; 
				while (heapmax = heap[1]) and (n > 0) do pop;
			end;
		end;
	end;
	t := 0;
	while n > 0 do
	begin
		if heap[1] <> heapmax then
		begin			
			inc(t);
			res[t] := heap[1];
			heapmax := heap[1];
		end;
		pop;
	end;
	writeln(t);
	for i := 1 to t do write(res[i], ' ');
end;

begin
	process;
end.
