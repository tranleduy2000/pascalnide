const
	maxn = 200000;
var
	bit: array[1..maxn] of longint;
	a: array[1..maxn] of longint;
	
procedure update(i, v: longint);
begin
	while i <= maxn do
	begin
		inc(bit[i], v);
		i := i + (i and -i);
	end;
end;

function get(i: longint): longint;
var
	res: longint;
begin
	res := 0;
	while i >= 1 do
	begin
		inc(res, bit[i]);
		i := i and (i-1);
	end;
	exit(res);
end;

procedure process;
var
	n, m, i, u, v: longint;
begin
	readln(n, m);
	fillchar(bit, sizeof(bit), 0);
	
	for i := 1 to n do 
	begin
		a[i] := n - i + 1;
		
	end;
	
	for i := 1 to m do
	begin
		read(v);
		n := n + 1;
		u := a[v];
		write((n -1 -u) -  (get(n-1) - get(u)), ' ');
		a[v] := n;
		update(u, 1);
	end;
end;

begin
	process;
end.
	
