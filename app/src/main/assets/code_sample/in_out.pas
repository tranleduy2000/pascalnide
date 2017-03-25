program test_io;
var a: integer;
	n: string;
begin
	write('enter your name: ');
	readln(n);
	write('your name is ', n);
	
	write('enter your age: ');
	readln(a);
	write('your age is ', a);

	writeln;
	//print multi variable to console
	writeln('name: ', n, ' age: ', a);
	readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}