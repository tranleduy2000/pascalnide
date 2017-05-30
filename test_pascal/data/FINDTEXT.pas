const maxn = round(1e7); base = round(1e9) + 11;
var s: ansistring;
	n, max, index, i, j: longint;
	h, pow: array[0..maxn] of qword;
	
function get(i, j: longint): qword;
var tmp: qword;
begin
	tmp := (h[j] - h[i-1] * pow[j-i + 1] + base * base) mod base;
	exit(tmp);
end;

begin
	readln(s);
	n := length(s);
	
	pow[0] := 1; h[0] := 0;
	for i := 1 to n do 
	begin
		pow[i] :=  (pow[i-1] * 10) mod base;
		h[i] := (h[i-1] * 10 + ord(s[i]) - ord('0')) mod base;
	end;
		
	index := 1;
	max := 0;
	
	for i := 1 to n div 2 do
	begin
		for j := i + 1 to n - i + 1 do
			if h[i] = get(j, j + i - 1) then
			begin
				max := i;
				index := j;
				break;
			end;
		if (i + j - 1 > n) then break;
	end;
	writeln(max, ' ', index);
end.
