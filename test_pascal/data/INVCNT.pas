const
	maxn = 10000000;
var
	t: array[1..maxn] of longint;
	max: longint;
	a: array[1..2000000] of longint;
		
function get(i: longint): longint;
var
	res: longint;
begin
	res := 0; 
	while i > 0 do
	begin 
		inc(res, t[i]);
		i := i and (i-1);
	end;
	exit(res);
end;

procedure update(i, v: longint);
begin
	while i <= max do
	begin
		inc(t[i],v);
		inc(i, i and -i);	
	end;
end;

procedure process;
var
	i, n, te:  longint;

	f: text;
	res: int64;
begin
	assign(f, 'file.inp');
	reset(f);
	readln(f, te);
	while te > 0 do
	begin
		readln(f, n);
		fillchar(t, sizeof(t), 0);
		res := 0; max := 0;
		for i := 1 to n do 
		begin
			readln(f, a[i]);
			if max < a[i] then max := a[i];
		end; 
		readln(f);
		for i := n downto 1 do
		begin
			inc(res, get(a[i]));
			update(a[i]+1, 1);
		end;
		writeln(reS);
		dec(te);
	end;
	close(f);
end;

begin
	process;
end.
