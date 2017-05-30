const
	dirx: array[1..4] of shortint = (-2, -2, 2, 2);
	diry: array[1..4] of shortint = (-2, 2, 2, -2);
var
	sy, sx, ty, tx: integer;
	front, rear: integer;
	qx, qy: array[1..64] of integer;
	c: array[1..8, 1..8] of integer;
	b: array[1..8, 1..8] of boolean;
	
procedure readf;
begin
	readln(sx, sy, tx, ty);
end;

procedure push(x, y: integer);
begin
	inc(rear);
	qx[rear] := x;
	qy[rear] := y;
end;

procedure pop(var x, y: integer);
begin
	x := qx[front];
	y := qy[front];
	inc(front);
end;

procedure BFS;
var
	u, v, i, mx, my: integer;
begin
	front := 1;
	rear := 0;
	push(sx, sy);
	fillchar(c, sizeof(c), 0);
	fillchar(b, sizeof(b), true);
	b[sx, sy] := false;
	repeat
		pop(u, v);
		for i := 1 to 4 do
		begin
			mx := u + dirx[i];
			my := v + diry[i];
			if (mx in [1..8]) and (my in [1..8]) and b[mx, my] then
			begin
				push(mx, my);
				b[mx, my] := false;
				c[mx, my] := c[u, v] + 1;
				if (mx = tx) and (my = ty) then exit;
			end;
		end;
	until front > rear;
end;

procedure result;
begin
	if b[tx, ty] then write(-1)
	else write(c[tx, ty]);
end;

begin
	readf;
	BFS;
	result;
end.
	
