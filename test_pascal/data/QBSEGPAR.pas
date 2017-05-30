const maxn = 15000; oo = 1000000000;
var
	s, fmin, fmax: array[1..maxn] of longint;
	n, k: longint;
	
function min(x, y: longint): longint;
begin if x <y then exit(x) else exit(y); end;

function max(x, y: longint): longint;
begin if x > y then exit(x) else exit(y); end;

function check(m: longint): boolean;
var	i, j: longint;
begin
	//writeln('check mid ', m);
	if s[1] <= m then 
	begin
		fmin[1] := 1;
		fmax[1] := 1
	end	else
	begin
		fmin[1] := oo;
		fmax[1] := -oo;
	end;
	for i := 2 to n do
	begin
		fmin[i] := oo;
		fmax[i] := -oo;
		j := i - 1;
		while (j > 0) and (s[i] - s[j] <= m) do
		begin
			fmin[i] := min(fmin[i], fmin[j] + 1);
			fmax[i] := max(fmax[i], fmax[j] + 1);
			dec(j);
		end;
	end;
	exit((fmin[n] <= k) and (k <= fmax[n]));
end;

procedure readf;
var
	f: text;
	i, a: longint;
begin
	assign(f, 'file.inp');
	reset(F);
	readln(f, n, k);
	for i := 1 to n do
	begin
		readln(f, a);
		s[i] := s[i-1] + a;
	end;
	close(f);
end;

procedure process;
var l, r, mid, res: longint;
begin
	l := -oo; r := oo;
	while l <= r do
	begin
		mid := (l + r) div 2;
		if check(mid) then
		begin
			res := mid;
			r := mid - 1;
		end else 
			l := mid + 1;
	end;
	writeln(res);
end;

begin
	readf;
	process;
end.
