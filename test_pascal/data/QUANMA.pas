const
	dirx: array[1..8] of shortint = (-2, -1, +1, +2, +2, +1, -1, -2);
	diry: array[1..8] of shortint = (+1, +2, +2, +1, -1, -2, -2, -1);
var
	n, m, x1, y1, x2, y2: longint;
	free: array[-1..1002, -1..1002] of boolean;
	dx, dy: array[1..1000000] of integer;
	c: array[1..1000, 1..1000] of longint;
	front, rear: longint;
	
procedure readf;
begin
	readln(n, m);
	readln(x1, y1);
	readln(x2, y2);
end;
	
procedure push(x, y: longint);
begin
	inc(rear);
	dx[rear] := x;
	dy[rear] := y;
end;

procedure pop(var x, y: longint);
begin
	x := dx[front];
	y := dy[front];
	inc(front);
end;
procedure BFS;
var
	x, y, tx, ty, i, j: longint;
begin
	front := 1;
	rear := 0;
	push(x1, y1);
	fillchar(c, sizeof(c), 0);
	fillchar(free, sizeof(free), false);
	for i := 1 to n do
		for j := 1 to m do free[i, j] := true;
	repeat
		pop(x, y);
		for i := 1 to 8 do
		begin
			tx := x + dirx[i];
			ty := y + diry[i];
			if free[tx, ty] then
			begin
				c[tx, ty] := c[x, y] + 1;
				if (tx = x2) and (ty = y2) then
				begin
					write(c[tx, ty]);
					readln;
					exit;
				end;
				free[tx, ty] := false;
				push(tx, ty);
			end; 
			
		end;
	until front > rear;
	write(-1);
end;

begin
	readf;
	BFS;
end.
