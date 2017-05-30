var
	n: byte;
	k, count: longint;
	sl: array[1..3] of byte;
	
procedure move(v, a, b: longint);
begin
	if v = 1 then
	begin
		inc(count);
		dec(sl[a]);
		inc(sl[b]);
		if count = k then
		begin
			write(sl[1],' ', sl[2], ' ', sl[3]); 
		end;
		//writeln('move ', a, ' to ', b);
	end
	else
	begin
		move(v - 1, a, 6 - a - b);
		move(1, a, b);
		move(v - 1, 6 - a - b, b);
	end;
end;

begin
	readln(n, k);
	sl[1] := n;
	move(n, 1, 2);
end.
