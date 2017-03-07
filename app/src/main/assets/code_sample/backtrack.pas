program loop;

function factorial(i: integer): integer;
begin
	if i = 1 then result := 1
	else result := factorial(i-1) * i;
end;

begin
	write('10! = ', factorial(10));
end.