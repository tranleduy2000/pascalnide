const
	maxn = 100000;
var
	bit: array[1..maxn * 2] of longint;
	n, nmax: longint;
	a: array[1..maxn] of longint;
	
procedure update(i, val: longint);
begin
	while i <= nmax do
	begin
		inc(bit[i], val);
		inc(i, i and -i);
	end;
end;

function get(i: longint): longint;
var
	res: longint;
begin
	res := 0;
	while i >= 1 do
	begin
		inc(res, bit[i]);
		i := i and (i - 1);
	end;
	get := res;
end;

procedure process;
var
	res: int64;
	i: longint;
begin
	
	readln(n);
	fillchar(bit, sizeof(bit), 0);
	res := 0;
	nmax := 0;
	
	for i := 1 to n do 
	begin
		read(a[i]);
		if a[i] > nmax then nmax := a[i];
	end;
	for i := n downto 1 do 
	begin
		res := res + get(a[i]);
		update(a[i] +1, 1);
	end;
	
	writeln(res);
end;

begin
	process;
end.
    
