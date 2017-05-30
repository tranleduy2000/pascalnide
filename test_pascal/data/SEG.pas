var
	n, sum: longint;
	a: array[1..1000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do readln(a[i]);
end;

function f(s: longint): boolean;
var
	t, m: longint;
begin
	t := 1;
	m := 0;
	while t <= n do
	begin
		m := m + a[t];
		if m = s then m := 0
		else if m > s then exit(false);
		inc(t);
	end;
	exit(true);
end;

procedure process;
var
	i: longint;
begin
	for i := 1 to n do sum := sum + a[i];
	for i := n downto 1 do
		if sum mod i = 0 then
		begin
			if f(sum div i) then
			begin
				write(i);
				exit;
			end;
		end;
	write(1);
end;

begin
	readf;
	process;
end.
