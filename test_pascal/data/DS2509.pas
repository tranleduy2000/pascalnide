const maxn = 10000;
var lab: array[1..maxn] of longint;
	p, i, j, u,r1, r2, v: longint;
	f: text;
function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var x:  longint;
begin
	x := lab[r1] + lab[r2];
	if lab[r1] < lab[r2] then
	begin
		lab[r1] := x;
		lab[r2] := r1;
	end else
	begin
		lab[r2] := x;
		lab[r1] := r2;
	end;
end;

begin
	for i := 1 to maxn do lab[i] := -1;
	assign(f, 'file.inp');
	reset(f);
	readln(f, P);
	for i := 1 to p do
	begin
		readln(f, u, v, j);
		if j = 1 then union(getroot(u), getroot(v))
		else begin
			r1 := getroot(u);
			r2 := getroot(v);
			if r1 = r2 then writeln(1) else writeln(0);
		end;
	end;
	close(f);
end.
