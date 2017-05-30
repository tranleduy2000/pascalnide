var
	t: longint;
	pow: array[0..63] of qword;	
function getbit(state: qword; j: byte): byte;
begin
	exit(state shr j and 1);
end;

procedure process;
var
	r, x: qword;
begin
	readln(t);
	while t > 0 do
	begin
		readln(x);
		r := 1;
		while r < x do r := r shl 1 or 1;
		writeln(x xor r);
		dec(t);
	end;
end;

begin
	process;
end.
