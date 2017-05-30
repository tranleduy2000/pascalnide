var
	s: string;
	i, n, j: longint;
	a: array[0..16] of integer;
begin
	readln(n);
	for i := 1 to 15 do a[i] := 0;
	for i := 1 to n do
	begin
		readln(s);
		for j := 1 to length(s) do a[j] := a[j] xor ord(s[j]);
	end;
	
	for i := 1 to 15 do
		if a[i] <> 0 then write(chr(a[i]))
		else
		begin
			if i = 1 then write(-1);
			break;
		end;
end.

{
* 1101010 
* 1010001 
* 1101010
* 
* 
  1010001 
* }


