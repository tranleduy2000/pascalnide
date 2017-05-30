const maxn = 100000;
var left, right: array[1..maxn] of longint;
	n, m: longint;
	
procedure init;
var  i: longint;
begin
	left[1] := n;
	for i := 2 to n do left[i] := i - 1; 
	right[n] := 1;
	for i := 1 to n - 1 do right[i] := i + 1;
end;

procedure process;
var u, v, i, id, tmp1, tmp2: longint;
begin
	for i := 1 to m do
	begin
		readln(u, v);
		left[right[u]] := left[u];
		right[left[u]] := right[u];
		
		right[u] := right[v];
		left[u] := v;
		
		left[right[v]] := u;
		right[v] := u;
	end;
	
	id := 1;
	v := n;
	while v > 0 do
	begin
		write(id, ' ');
		id := right[id];
		dec(v);
	end;
end;
begin
	readln(n, m);
	init;
	process;
end.
