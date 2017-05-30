var
	bit: array[1..1000, 1..1000] of int64;

	n: longint;
	
procedure update(x, y, v: longint);
var
	tmp: longint;
begin
	tmp := y;
	while x <= n do
	begin
		tmp := y;
		while tmp <= n do
		begin
			inc(bit[x, tmp], v);
			inc(tmp, tmp and -tmp);
		end;
		inc(x, x and -x);
	end;
end;

function get(x, y: longint): int64;
var
	res, t: int64;
begin
	res := 0;
	while x > 0 do
	begin
		t := y;
		while t > 0 do
		begin
			inc(res, bit[x, t]);
			dec(t, t and -t);
		end;
		dec(x, x and -x);
	end;
	exit(res);
end;

procedure readf;
var
	i, j, a: longint;
begin
	//assign(f, 'file.inp');
	//reset(f);
	readln( n);
	for i := 1 to n do 
		for j := 1 to n do 
		begin
			read(a);
			update(i, j, a);
		end;
end;

procedure process;
var
	c, x, y,q, i, v, u: longint;
	
begin
	readln( q);
	for i := 1 to q do
	begin
		read( c);
		if c = 1 then
		begin
			readln(x, y, v);
			update(x, y, v)
		end
		else 
		begin
			readln(x, y, u, v);
			writeln(get(u, v) - get(u, y-1) - get(x-1, v) + get(x-1, y-1));
		end;
	end;
	
end;

begin
	readf;
	process;

end.
