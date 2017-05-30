const maxn = 1500;
type edge = record u, v: longint; mark: boolean; end; int = longint;
var e: array[1..maxn] of edge;
	lab: array[1..maxn] of longint;
	n, m, count: longint; 
	
function getroot(v: longint): longint;
begin
	while lab[v] > 0 do v := lab[v];
	exit(v);
end;

procedure union(r1, r2: longint);
var x: int;
begin
	x :=lab[r1] + lab[r2];
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

procedure init;
var i: longint;
	f: text;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n, m);
	for i := 1 to m do
		begin 
			readln(f, e[i].u, e[i].v); 
			e[i].mark := true; 
		end;
	close(f);
end;

function solve: boolean;
var r1, r2, c, i: longint;
begin
	c := 0;
	for i := 1 to n do lab[i] := -1; //init 
	
	for i := 1 to m do //xet het
		if e[i].mark then  //ko phai la canh bo
		begin
			r1 := getroot(e[i].u);
			r2 := getroot(e[i].v);
			if r1 <> r2 then
			begin
				union(r1, r2);
				e[i].mark := false;
				inc(c);
				if c = n - 1 then exit(true);
			end;
		end;
	exit(false);
end;

procedure process;
var i, j: longint;
	b: boolean;
begin
	writeln(3);
	for i := 1 to m do
	begin
		//writeln('spaning tree ', i);
		e[i].mark := false;
		b := solve;
		if b then 
		begin
			for j := 1 to m do 
				if (e[j].mark = false) and (j <> i) then
					writeln(e[j].u, ' ', e[j].v);
			inc(count);
		end;
		
		for j := 1 to m do e[j].mark := true;
		if count = 3 then break;
	end;
end;
begin
	init;
	process;
end.










