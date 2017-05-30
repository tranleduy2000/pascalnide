var
	n: longint;
	h: array[1..100] of longint;
//heap max	
procedure swap(var x, y: longint);
var tmp: longint;
begin
	tmp := x; 
	x := y; 
	y := tmp;
end;
procedure up(i: longint);
begin
	if (i = 1) or (h[i] <= h[i div 2]) then exit; //root or child < parent
	swap(h[i], h[i div 2]);
	up(i div 2);
end;

procedure down(r: longint);
var c: longint;
begin
	c := r * 2;
	if c > n then exit;
	if (c < n) and (h[c] < h[c + 1]) then inc(c);
	if h[r] <= h[c] then
	begin
		swap(h[r], h[c]);
		down(c);
	end;
end;

procedure pop;
begin
	h[1] := h[n];
	dec(n);
	down(1);
end;

procedure print;
var i: longint;
begin
	for i := 1 to n do write(h[i], ' '); writeln;
end;

procedure push(v: longint);
begin
	inc(n);
	h[n] := v;
	up(n);
end;

procedure process;
var i, tmp, v: longint;
begin
	readln(tmp); n := 0;
	for i := 1 to tmp do 
	begin
		read(v);
		push(v);
	end;
	
	for i := 1 to n - 1 do
	begin
		print;
		pop;
	end;
	print;
end;

begin
	process;
end.
