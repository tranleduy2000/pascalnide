var
	n: longint;
	
function f(x, y: longint): boolean;
var
	m: array[0..9] of boolean;
begin	
	fillchar(m, sizeof(m), false);
	while x > 0 do
	begin
		m[x mod 10] := true;
		x := x div 10;
	end;
	while y > 0 do
	begin
		if m[y mod 10] then exit(false);
		y := y div 10;
	end;
	exit(true);
end;

procedure process;
var
	i: longint;
	a: longint;
begin
	a := 0;
	i := 1;
	dec(n);
	while n > 0 do 
	begin
		if f(a, i) then
		begin
			a := i;
			dec(n);
			write(a, ' ');
		end;
		inc(i);
	end;
	writeln(a);
end;

begin
	//readln(n);
	n := 300;
	process;
end.
