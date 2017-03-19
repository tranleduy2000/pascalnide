program test_str;
const 
	number = 1101010;
var 
	s: string;
begin
	str(number, s);
	writeln('number is ', number);
	writeln('string is ', s);
	readln;

end.