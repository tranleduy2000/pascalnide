var
	s, s1: string;
	i, j: integer;
	f: array[0..101, 0..6] of integer;
	
function max(x, y: integer): integer;
begin
	if x > y then exit(x) else exit(y);
end;

begin
	s := 'hello';
	readln(s1);
	fillchar(f, sizeof(f), 0);
	for i := 1 to length(s1) do
		for j := 1 to 5 do
			if s1[i] = s[j] then f[i, j] := f[i-1, j-1] + 1
			else f[i, j] := max(f[i-1, j], f[i, j-1]);
			
	if f[length(s1), 5] = 5 then write('YES')
	else write('NO');
end.
