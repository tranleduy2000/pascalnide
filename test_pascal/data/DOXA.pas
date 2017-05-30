var
	n: byte;
	a, b, s: array[0..100] of integer;

procedure readf;
var
	i: byte;
begin
	readln(n);
	a[1] := 0;
	for i := 1 to n-1 do read(a[i]);
	for i := 1 to n do read(b[i]);
	n := n - 1;
end;

function min(j: byte): byte;
var
	i, index: byte;
begin
	index := 1;
	for i := 2 to j do if b[i] < b[index] then index := i;
	exit(index);
end;

procedure init;
var
	i: longint;
begin
	s[0] := 0;
	for i := 1 to n do s[i] := s[i-1] + a[i];
end;

procedure process;
var
	j, index, res: longint;
begin
	j := n;
	res := 0;
	while j > 0 do
	begin
		index := min(j);
		res := res + b[index] *(s[j] - s[index-1]);
		//writeln(index,' j = ', j , ' res = ', res);
		j := index - 1;	
	end;
	write(res);
end;
begin
	readf;
	init;
	process;
end.
