const
	dirx: array[1..4] of shortint = (1, -1, 0, 0);
	diry: array[1..4] of shortint = (0, 0, 1, -1);
var
	a: array[0..10, 0..10] of boolean;
	n, sx, sy, dx, dy: integer;
	found: boolean;
	
procedure readf;
var
	i, j, c: integer;
begin
	readln(n, sx, sy, dx, dy);
	fillchar(a, sizeof(a), 0);
	for i := 1to n do
		for j := 1 to n do 
		begin
			read(c);
			if c = 1 then a[i, j] := false
			else a[i, j] := true;
		end;
end;

procedure DFS(x, y: integer);
var
	i, u, v: integer;
begin
	if found then exit;
	if (dx = x) and (dy = y) then
	begin
		found := true;
		exit;
	end;
	a[x, y] := false;
	for i := 1 to 4 do
	begin
		u := x + dirx[i];
		v := y + diry[i];
		if a[u, v] then DFS(u, v);
	end;
end;
begin
	readf;
	found := false;
	DFS(sx, sy);
	if found then write('YES')
	else write('NO');
end.
