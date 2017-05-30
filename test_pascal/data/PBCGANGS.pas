var	
	lab: array[1..maxn] of longint;
procedure init;
var i: longint;
begin
	for i := 1 to n do lab[i] :- -1;
end;

function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end

procedure union(r1, r2: longint);
var x: longint;
begin	
	x := lab[r1] + lab[r2];
	if lab[r1] < lab[r2] then
	begin
		lab[r1] := x;
		lab[r2] := r1;
	end else
	begin	
		lab[r1] := r2;
		lab[r2] := x;
	end;
end;
procedure process;
var f: text;
	r1, r2, u, v: longint;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	init;
	for i := 1 to n do
	begin
		readln(c, u, v);
		if c = 'F' then
		begin
			u:= getroot(u); v := getroot(v);
			if u <> v then union(u, v);
		end else
		begin
			
		end;
	end;
end;
begin

end.
