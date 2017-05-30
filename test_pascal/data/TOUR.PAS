const maxn = 2000000;
type
	TNode = ^Pnode;
	pnode = record
		u: longint;
		next: Tnode;
	end;
	
var
	e: array[1..maxn] of tnode;
	n: longint;
procedure init;
var i: longint;
begin
	for i := 1 to n do e[i] := nil;
end;

procedure push(p, r: longint);
var tmp: tnode;
begin
	new(tmp);
	tmp^.u := r;
	tmp^.next := e[p];
	e[p] := tmp;
end;

procedure readf;
var f: text;
	i: integer;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, n);
	init();
	for i := 1 to n do
	begin
		readln(f, u, v);
		push(u, v);
		push(v, u);
	end;
	readln(f, m);
	close(f);
end;

procedure 

begin
end.
	
