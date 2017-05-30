const maxn = 100001;
var bottom, up: array[0..maxn] of longint;
	top, n, m: longint;
	
procedure init;
var  i: longint;
begin
	for i := 1 to n do up[i] := i - 1; 
	for i := 1 to n - 1 do bottom[i] := i + 1;
	
	top := 1;
end;

procedure process;
var u, v, i: longint;
begin
	for i := 1 to m do
	begin
		read(u);
		if u <> top then
		begin
			up[bottom[u]] := up[u];
			bottom[up[u]] := bottom[u];
			
			bottom[u] := top;
			up[top] := u;
			
			top := u;
		end;
	end;
	
	v := n;
	while v > 0 do
	begin
		write(top, ' ');
		top := bottom[top];
		dec(v);
	end;
end;
begin
	readln(n, m);
	init;
	process;
end.
