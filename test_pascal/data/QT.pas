const
	dirx : array[1..4] of shortint = (-1, -1, 1, 1);
	diry : array[1..4] of shortint = (-1, 1, 1, -1);
	maxC = 10000000;
var
	a: array[0..1000, 0..1000] of boolean;
	n, m, p, q, s, t, front, rear: longint;
	qx, qy: array[1..1000000] of longint;
	c: array[0..1000, 0..1000] of longint;
	
procedure readf;
var
	u, v, i: longint;
begin
	readln(n, m, p, q, s, t);
	fillchar(a, sizeof(a), true);
	for i := 1 to m do
	begin
		readln(u, v);
		a[u, v] := false;
	end;
end;

procedure push(x, y: longint);
begin
	inc(rear);
	qx[rear] := x;
	qy[rear] := y;
end;

procedure pop(var x, y: longint);
begin
	x := qx[front];
	y := qy[front];
	inc(front);
end;

function min(x, y: longint): longint;
begin
	if x < y then exit(x) else exit(y);
end;

function range(x, y: longint): boolean;
begin
	if (x >= 1) and (x <= n) and (y >= 1) and (y <= n) and (a[x, y]) then exit(true);
	exit(false);
end;

procedure BFS;
var
	u, v, x, y, i: longint;
begin
	front := 1;
	rear := 0;
	push(p, q);
	for u := 1 to n do 
		for v := 1to n do c[u, v] := maxC;
	c[p, q] := 0;
	a[p, q] := false;
	repeat
		pop(u, v);
		for i := 1 to 4 do
		begin
			x := u;
			y := v;
			while range(x + dirx[i], y + diry[i]) do
			begin
				x := x + dirx[i];
				y := y + diry[i];
				a[x, y] := false;
				c[x, y] := min(c[x, y], c[u, v] + 1);
				push(x, y);
			end;
		end;
	until front > rear;
	
	{for u := 1to n do
	begin
		for v := 1 to n do 
			if c[u, v] = maxC then write('X ') else write(c[u, v], ' ');
		writeln;
	end;}
	if c[s, t] <> maxC then write(c[s, t])
	else write(-1);
end;

begin
	readf;
	BFS;
end.
