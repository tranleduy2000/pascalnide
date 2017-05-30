var
	n: longint;
	s, t: array[1..1000] of longint;
	
procedure readf;
var
	i: longint;
begin
	readln(n);
	for i := 1 to n do readln(s[i], t[i]);
end;

function f(i, j: longint): boolean;
begin
	exit((s[i] >= t[j]) or (t[i] <= s[j])
		or (s[j] >= t[i]) or (t[j] <= s[i]));
end;
procedure process;
var
	i, j, count: longint;
begin	
	count := 0;
	for i := 1 to n-1  do
		for j := i+1 to n do if not f(i, j) then inc(count);
	writeln(count);
end;

begin
	readf;
	process;
end.
