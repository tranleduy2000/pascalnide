var
	a: array[1..200] of longint;
	
function d(x: int64): int64;
var	 
	tmp, t2: int64;
begin
    t2 := 0;
	while x > 0 do
	begin
		t2 := t2 * 10 + x mod 10;
		x := x div 10;
	end;
	exit(t2);
end;


procedure process;
var
	i: longint;
	x: int64;
begin
	a[1] := 1;
	for i := 2 to 82 do a[i] := d(a[i-1]) + 2;
//.  for i := 1 to 200 do writeln(i, ' ', a[i], ' ');
	readln(x);
	if x <= 82 then write(a[x])
	else if x mod 81 = 1 then write(10)
	else if x mod 81 = 0 then write(a[81])
	else write(a[(x) mod 81]);
end;

begin
	process;	
end.
