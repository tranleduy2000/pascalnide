const base = round(1e9) + 11;
var
	ha, pow: array[0..1000000] of qword;
	hB: qword;
	n, m: longint;
	b, a: ansistring;
	
procedure precalc;
var i: longint;
begin
	pow[0] := 1;
	for i := 1 to n do pow[i] := (pow[i-1] * 26) mod base;
	
	hb := 0;
	for i := 1 to m do hb := (hb * 26 + ord(b[i]) - ord('a')) mod base;
	
	ha[0] := 0;
	for i := 1 to n do ha[i] := (ha[i-1] * 26 + ord(a[i]) - ord('a')) mod base;
end;

function get(i, j: longint): qword;
var tmp: qword;
begin
	tmp := (ha[j] - ha[i-1] * pow[j-i + 1] + base * base) mod base;
	exit(tmp);
end;

procedure process;
var i: longint;
begin
	readln(a);	readln(b);
	n := length(a); m := length(b);
	precalc;
	for i := 1 to n - m + 1 do
		if hb = get(i, i + m - 1) then write(i, ' ');
end;

begin
	process;
end.


