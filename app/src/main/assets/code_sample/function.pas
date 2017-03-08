program test_func;

//return sqrt(number)
function square_root(a: integer): real;
begin
	square_root := sqrt(a);
end;

begin
	writeln('sqrt(4) = ', square_root(4));
end.