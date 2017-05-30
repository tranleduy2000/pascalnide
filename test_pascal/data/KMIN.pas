const maxn = 50000;
type node = record i, j, value: longint; end;
var
	a, b: array[1..maxn] of longint;
	heap: array[1..maxn] of node;
	nheap: longint;

procedure swap(u, v: longint);
var
	tmp: node;
begin
	tmp := heap[u];
	heap[u] := heap[v];
	heap[v] := tmp;
end;

procedure upheap(u: longint);	
begin
	if u = 1 then exit;
	if heap[u].value < heap[u div 2].value then
	begin
		swap(u, u div 2);
		upheap(u div 2);
	end;
end;

procedure downheap(u: longint);
var
	c: longint;
begin
	c := u * 2;
	if c > nheap then exit;
	if (c < nheap) and (heap[c].value > heap[c+1].value) then inc(c);
	if heap[u].value > heap[c].value then
	begin
		swap(u, c);
		downheap(c);
	end;
end;

procedure push(i, j: longint);
var 
	tmp: node;
begin
	tmp.value := a[i] + b[j];
	tmp.i := i;
	tmp.j := j;
	
	inc(nheap);
	heap[nheap] := tmp;
	upheap(nheap);
end;

function pop: node;
begin
	pop := heap[1];
	heap[1] := heap[nheap];
	dec(nheap);
	downheap(1);
end;

procedure sorta(l, r: longint);
var
	k,i, j, tmp: longint;
begin
	i := l;
	j := r;
	k := a[(l + r) div 2];
	repeat
		while a[i] < k do inc(i);
		while a[j] > k do dec(j);
		if i <= j then
		begin
			tmp := a[i];
			a[i] := a[j];
			a[j] := tmp;
			inc(i); dec(j);
		end;
	until i > j;
	if i < r then sorta(i, r);
	if l < j then sorta(l ,j);
end;

procedure sortb(l, r: longint);
var
	k,i, j, tmp: longint;
begin
	i := l;
	j := r;
	k := b[(l + r) div 2];
	repeat
		while b[i] < k do inc(i);
		while b[j] > k do dec(j);
		if i <= j then
		begin
			tmp := b[i];
			b[i] := b[j];
			b[j] := tmp;
			inc(i); dec(j);
		end;
	until i > j;
	if i < r then sortb(i, r);
	if l < j then sortb(l , j);
end;

procedure process;
var
	f: text;
	i,  n, m, k: longint;
	tmp: node;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, m, n, k);
	for i := 1 to m do read(f, a[i]);
	for i := 1 to n do read(f, b[i]);
	close(f);

	sorta(1, m);
	sortb(1, n);
	nheap := 0;
	for i := 1 to n do push(1, i);
		
	for i := 1 to k do
	begin
		tmp := pop;
		writeln( tmp.value);
		if tmp.i <= m then push(tmp.i + 1, tmp.j);
	end;
end;

begin
	process;
end.
